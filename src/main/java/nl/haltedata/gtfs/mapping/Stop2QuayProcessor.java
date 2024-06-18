package nl.haltedata.gtfs.mapping;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.batch.item.ItemProcessor;

import nl.haltedata.gtfs.dto.GtfsCsvStop;
import nl.haltedata.gtfs.dto.GtfsQuay;
import nl.haltedata.gtfs.dto.RefIfoptFactory;

public class Stop2QuayProcessor implements ItemProcessor<GtfsCsvStop, GtfsQuay> {
 
    private final GeometryFactory geometryFactory;

    public Stop2QuayProcessor(GeometryFactory geometryFactory) {
        super();
        this.geometryFactory = geometryFactory;
    }

    @Override
    public GtfsQuay process(GtfsCsvStop stop) throws Exception {
        
        // Skip stop areas
        if (stop.getLocationType() == 1) return null;
        
        // Skip railway stations
        if (stop.getZoneId().startsWith("IFF:")) return null;
        
        var quay = new GtfsQuay();
        
        quay.setQuayId(Long.valueOf(stop.getStopId()));
        quay.setQuayCode(stop.getStopCode());
        String[] stopName = stop.getStopName().split(", ", 2);
        if (stopName.length > 1) {
            quay.setPlace(stopName[0]);
            quay.setName(stopName[1]);
        }
        else {
            quay.setName(stopName[0]);            
        }
        double lat = stop.getStopLat(); 
        double lon = stop.getStopLon(); 
        Point latLon = geometryFactory.createPoint(new Coordinate(lon, lat));
        
        quay.setCoordinates(latLon);
        String parentStation = stop.getParentStation();
        if (!parentStation.isEmpty()) {
            String[] parts = parentStation.split(":", 2);
            quay.setStopAreaId(Long.valueOf(parts[1]));
        }
        Integer wcBoarding = stop.getWheelchairBoarding();
        if (wcBoarding != null) quay.setWheelchairBoarding(wcBoarding.equals(1));
        quay.setRefIfopt(RefIfoptFactory.createIfopt(quay));
        return quay;
    }
}
