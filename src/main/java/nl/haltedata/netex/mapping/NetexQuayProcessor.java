package nl.haltedata.netex.mapping;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.rutebanken.netex.model.ScheduledStopPoint;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBElement;
import nl.haltedata.netex.dto.NetexQuay;
import nl.haltedata.tools.RdToWgs84Transformation;

@Component
public class NetexQuayProcessor implements ItemProcessor<JAXBElement<ScheduledStopPoint>, NetexQuay> {
 
    @Inject
    @Nonnull
    private GeometryFactory geometryFactory;
    private final RdToWgs84Transformation rdToWgs = new RdToWgs84Transformation();
    
    @Override
    public NetexQuay process(JAXBElement<ScheduledStopPoint> element) throws Exception {
        var stop = element.getValue();
        var quay = new NetexQuay();
        quay.setId(stop.getId());
        if (stop.getName() != null) {
            quay.setName(stop.getName().getValue());
            var parts = stop.getName().getValue().split(", ", 2);
            if (parts.length ==2) {
                quay.setPlace(parts[0]);
                quay.setName(parts[1]);
            }
            else {
                quay.setName(parts[0]);                
            }
        }
        if (stop.getShortName() != null) {
            quay.setShortName(stop.getShortName().getValue());
        }
        if (stop.getPrivateCode() != null && "UserStopCode".equals(stop.getPrivateCode().getType())) {
            quay.setUserStopCode(stop.getPrivateCode().getValue());
            quay.setRefIfopt(String.format("NL:Q:%s", stop.getPrivateCode().getValue()));
        }
        var rd = stop.getLocation();
        if (rd != null) {
            var pos = rd.getPos().getValue();
            Coordinate coordinate = new Coordinate(pos.get(0), pos.get(1));
            Point rdLocation = geometryFactory.createPoint(coordinate);
            rdLocation.setSRID(28992);
            quay.setRdLocation(rdLocation);
            quay.setWgsLocation(rdToWgs.transform(rdLocation));
        }
        if (stop.getTariffZones() != null) {
            var zoneRefs = stop.getTariffZones().getTariffZoneRef_();
            List<String> zones = new ArrayList<>(zoneRefs.size());
            zoneRefs.forEach(z -> {
                var zoneRef = z.getValue();
                String[] parts = zoneRef.getRef().split(":");
                if (parts.length == 3 && "DOVA".equals(parts[0])) {
                    zones.add(parts[2]);
                }
            });
            quay.setTariffZones(String.join(";", zones));
        }
        return quay;       
    }
}