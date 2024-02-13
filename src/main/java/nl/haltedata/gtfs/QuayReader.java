package nl.haltedata.gtfs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.function.Consumer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.locationtech.jts.geom.GeometryFactory;

import nl.haltedata.gtfs.dto.GtfsQuay;
import nl.haltedata.gtfs.mapping.QuayMapper;

public class QuayReader {
    final private static String[] header = {"stop_id","stop_code","stop_name","stop_lat","stop_lon","location_type",
                        "parent_station","stop_timezone","wheelchair_boarding","platform_code","zone_id"};

    // TODO Inject
    private GeometryFactory geometryFactory = new GeometryFactory();
    private QuayMapper quayMapper = new QuayMapper(geometryFactory);

    public void read(File source, Consumer<GtfsQuay> quayConsumer) {
        try (Reader reader = new BufferedReader(new FileReader(source));
             CSVParser parser = new CSVParser(reader, getFormat());
            ) {
            var it = parser.iterator();
            // Check the file header
            if (!checkHeader(it.next())) throw new RuntimeException("The header of the source file doesn't match the expected format");
            // Process the content
            it.forEachRemaining(rec -> {
                // Check location_type    
                switch (rec.get(5)) {
                case "0": // quay or railway station
                    String zoneId = rec.get(10);
                    if (zoneId != null && zoneId.startsWith("IFF:")) {
                        readRailwayStation(rec);
                    }
                    else {
                        var quay = quayMapper.map(rec);
                        quayConsumer.accept(quay);
                    }
                    break;
                case "1":
                    readStopArea(rec);
                    break;
                default:
                    throw new RuntimeException(String.format("Unknown location type: %s", rec.get(5)));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readStopArea(CSVRecord rec) {
    }

    private static void readRailwayStation(CSVRecord rec) {
        // TODO Auto-generated method stub
        
    }

    private static CSVFormat getFormat() {
        return Builder.create(CSVFormat.DEFAULT)
                .setHeader(header)
                .setNullString("")
                .build();
    }

    private static boolean checkHeader(CSVRecord headerRecord) {
        return Arrays.equals(headerRecord.values(), header);
    }
}
