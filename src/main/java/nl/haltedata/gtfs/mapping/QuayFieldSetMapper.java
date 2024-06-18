package nl.haltedata.gtfs.mapping;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.boot.context.properties.bind.BindException;

import nl.haltedata.gtfs.dto.GtfsQuay;
import nl.haltedata.gtfs.dto.RefIfoptFactory;

public class QuayFieldSetMapper implements FieldSetMapper<GtfsQuay> {
 
    private GeometryFactory geometryFactory;

    public QuayFieldSetMapper(GeometryFactory geometryFactory) {
        super();
        this.geometryFactory = geometryFactory;
    }

    @Override
    public GtfsQuay mapFieldSet(FieldSet fieldSet) throws BindException {
        var quay = new GtfsQuay();
        
        quay.setQuayId(fieldSet.readLong(0));
        quay.setQuayCode(fieldSet.readString(1));
        String[] stopName = fieldSet.readString(2).split(", ", 2);
        if (stopName.length > 1) {
            quay.setPlace(stopName[0]);
            quay.setName(stopName[1]);
        }
        else {
            quay.setName(stopName[0]);            
        }
        double lat = fieldSet.readDouble(3); 
        double lon = fieldSet.readDouble(4); 
        Point latLon = geometryFactory.createPoint(new Coordinate(lon, lat));
        
        quay.setCoordinates(latLon);
        String parentStation = fieldSet.readString(6);
        if (!parentStation.isEmpty()) {
            String[] parts = parentStation.split(":", 2);
            quay.setStopAreaId(Long.valueOf(parts[1]));
        }
        String wcBoarding = fieldSet.readString(8);
        if (wcBoarding != null) quay.setWheelchairBoarding(wcBoarding.equals("1"));
        quay.setRefIfopt(RefIfoptFactory.createIfopt(quay));
        return quay;
    }
}
