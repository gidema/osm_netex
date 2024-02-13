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

import nl.haltedata.gtfs.dto.GtfsStopTime;
import nl.haltedata.gtfs.mapping.StopTimeMapper;

public class StopTimeReader {
    final private static String[] header = {"trip_id","stop_sequence","stop_id","stop_headsign","arrival_time","departure_time",
            "pickup_type","drop_off_type","timepoint","shape_dist_traveled","fare_units_traveled"};

    private StopTimeMapper mapper = new StopTimeMapper();

    public void read(File source, Consumer<GtfsStopTime> consumer) {
        try (Reader reader = new BufferedReader(new FileReader(source));
             CSVParser parser = new CSVParser(reader, getFormat());
            ) {
            var it = parser.iterator();
            // Check the file header
            if (!checkHeader(it.next())) throw new RuntimeException("The header of the source file doesn't match the expected format");
            // Process the content
            it.forEachRemaining(rec -> {
                var stopTime = mapper.map(rec);
                consumer.accept(stopTime);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static CSVFormat getFormat() {
        return Builder.create(CSVFormat.DEFAULT)
                .setHeader(header)
                .build();
    }

    private static boolean checkHeader(CSVRecord headerRecord) {
        return Arrays.equals(headerRecord.values(), header);
    }
}
