package nl.haltedata.gtfs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.haltedata.gtfs.dto.GtfsQuay;
import nl.haltedata.gtfs.dto.GtfsQuayRepository;

public class GtfsStopsWriter {
    final private static String SOURCE_FOLDER ="/home/gertjan/Downloads";
    final private static int BATCH_SIZE = 100;
    final private List<GtfsQuay> buffer = new ArrayList<>(BATCH_SIZE);
    private long count = 0;
    
    private Logger logger = LoggerFactory.getLogger(GtfsStopsWriter.class);

    private GtfsQuayRepository quayRepository;

    public GtfsStopsWriter(GtfsQuayRepository quayRepository) {
        super();
        this.quayRepository = quayRepository;
    }

    public void run() {
        var stopsFile = new File(SOURCE_FOLDER, "stops.txt");
        var quayReader = new QuayReader();
        quayReader.read(stopsFile, this::handleQuay);
        // Save remaining buffer content
        quayRepository.saveAll(buffer);
        buffer.clear();
        logger.info("Saved {} GTFS quays", count);
    }

    private void handleQuay(GtfsQuay quay) {
        buffer.add(quay);
        count++;
        if (buffer.size() == BATCH_SIZE) {
            // buffer is full. Write 
            quayRepository.saveAll(buffer);
            buffer.clear();
        }
    }
}
