package nl.haltedata.gtfs.mapping;

import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import nl.haltedata.gtfs.dto.GtfsQuay;
import nl.haltedata.gtfs.dto.RefIfoptFactory;
import nl.haltedata.tools.CsvMapper;

public class QuayMapper implements CsvMapper<GtfsQuay> {

    private final GeometryFactory geometryFactory;
    private final Map<Long, String> operators;

    // switch to injection
    public QuayMapper(GeometryFactory geometryFactory, Map<Long, String> operators) {
        super();
        this.geometryFactory = geometryFactory;
        this.operators = operators;
    }

    @Override
    public GtfsQuay map(CSVRecord rec) {
        var quay = new GtfsQuay();
        
        quay.setQuayId(getLong(rec, 0));
        quay.setQuayCode(getString(rec, 1));
        String[] stopName = getString(rec, 2).split(", ", 2);
        if (stopName.length > 1) {
            quay.setPlace(stopName[0]);
            quay.setName(stopName[1]);
        }
        else {
            quay.setName(stopName[0]);            
        }
        double lat = getDouble(rec, 3); 
        double lon = getDouble(rec, 4); 
        Point latLon = geometryFactory.createPoint(new Coordinate(lon, lat));
        
        quay.setCoordinates(latLon);
        String parentStation = getString(rec, 6);
        if (parentStation != null) {
            String[] parts = parentStation.split(":", 2);
            quay.setStopAreaId(Long.valueOf(parts[1]));
        }
        String wcBoarding = getString(rec, 8);
        if (wcBoarding != null) quay.setWheelchairBoarding(wcBoarding.equals("1"));
        quay.setRefIfopt(RefIfoptFactory.createIfopt(quay));
        quay.setOperator(operators.get(quay.getQuayId()));
        return quay;
    }

}
