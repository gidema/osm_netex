package nl.haltedata.gtfs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.haltedata.gtfs.dto.GtfsStopTime;
import nl.haltedata.gtfs.dto.GtfsStopTimeRepository;

public class GtfsStopTimesWriter {
    final private static String SOURCE_FOLDER ="/home/gertjan/Downloads";
    final private static int BATCH_SIZE = 5000;
    final private List<GtfsStopTime> buffer = new ArrayList<>(BATCH_SIZE);
    private long count = 0;
    
    private Logger logger = LoggerFactory.getLogger(GtfsStopTimesWriter.class);

    private GtfsStopTimeRepository repository;

    public GtfsStopTimesWriter(GtfsStopTimeRepository repository) {
        super();
        this.repository = repository;
    }

    public void run() {
        var file = new File(SOURCE_FOLDER, "stop_times.txt");
        var stopTimeReader = new StopTimeReader();
        stopTimeReader.read(file, this::handleStopTime);
        // Save remaining buffer content
        repository.saveAll(buffer);
        buffer.clear();
        logger.info("Saved {} GTFS stop times", count);
    }

    private void handleStopTime(GtfsStopTime st) {
        buffer.add(st);
        count++;
        if (buffer.size() == BATCH_SIZE) {
            // buffer is full. Write
            repository.saveAll(buffer);
            buffer.clear();
        }
    }
}
