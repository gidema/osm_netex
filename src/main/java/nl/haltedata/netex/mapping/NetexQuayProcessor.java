package nl.haltedata.netex.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.rutebanken.netex.model.PointProjection;
import org.rutebanken.netex.model.ScheduledStopPoint;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import nl.haltedata.netex.dto.NetexQuay;
import nl.haltedata.tools.RdToWgs84Transformation;

@Component
public class NetexQuayProcessor implements ItemProcessor<ScheduledStopPoint, NetexQuay> {
 
    @Inject
    @Named("rdGeometryFactory")
    @Nonnull
    private GeometryFactory rdGeometryFactory;
    
    @Inject
    @Nonnull
    private RdToWgs84Transformation rdToWgs;
    
    @Override
    public NetexQuay process(ScheduledStopPoint stop) throws Exception {
        var quay = new NetexQuay();
        quay.setId(stop.getId());
        var stopName = getStopName(stop);
        quay.setName(stopName.name());
        quay.setPlace(stopName.place());
        if (stop.getShortName() != null) {
            quay.setShortName(stop.getShortName().getValue());
        }
        if (stop.getPrivateCode() != null && "UserStopCode".equals(stop.getPrivateCode().getType())) {
            quay.setUserStopCode(stop.getPrivateCode().getValue());
            quay.setUserStopOwnerCode(getOwnerCodeFromId(stop.getId()));
        }
        var rdLocation = getRdPoint(stop);
        quay.setRdLocation(rdLocation);
        quay.setWgsLocation(rdToWgs.transform(rdLocation));
        quay.setRoutePointRef(getRoutePointRef(stop));
        quay.setTariffZones(String.join(";", getTariffZones(stop)));
        return quay;
    }
    
    private static StopName getStopName(ScheduledStopPoint stop) {
        if (stop.getName() == null) return null;
        var parts = stop.getName().getValue().split(", ", 2);
        if (parts.length ==2) {
            return new StopName(parts[1], parts[0]);
        }
        return new StopName(parts[0], null);                
    }
    
//        <projections>
//        <PointProjection id="QBUZZ:PointProjection:50002
//400">
//            <ProjectToPointRef nameOfRefClass="RoutePoin
//t" ref="QBUZZ:RoutePoint:50002400"/>
//        </PointProjection>
//    </projections>
    private Point getRdPoint(ScheduledStopPoint stop) {
        var rd = stop.getLocation();
        if (rd == null) return null;
        var pos = rd.getPos().getValue();
        Coordinate coordinate = new Coordinate(pos.get(0), pos.get(1));
        return rdGeometryFactory.createPoint(coordinate);
    }
    
    private static String getRoutePointRef(ScheduledStopPoint stop) {
        if (stop.getProjections() != null) {
            for (var jaxbElement : stop.getProjections().getProjectionRefOrProjection()) {
                if (jaxbElement.getDeclaredType().equals(PointProjection.class)) {
                    var pointProjection = (PointProjection)jaxbElement.getValue();
                    var ptpRef = pointProjection.getProjectToPointRef();
                    return (ptpRef == null ? null : ptpRef.getRef());
                }
            }
        }
        return null;
    }
    
    private static List<String> getTariffZones(ScheduledStopPoint stop) {
        if(stop.getTariffZones() == null) return Collections.emptyList(); 
        var zoneRefs = stop.getTariffZones().getTariffZoneRef_();
        List<String> zones = new ArrayList<>(zoneRefs.size());
        zoneRefs.forEach(z -> {
            var zoneRef = z.getValue();
            String[] parts = zoneRef.getRef().split(":");
            if (parts.length == 3 && "DOVA".equals(parts[0])) {
                zones.add(parts[2]);
            }
        });
        return zones;
    }
    
    private static String getOwnerCodeFromId(String id) {
        return id.split(":")[0];
    }
    
    static record StopName(String name, String place) {}
}