package nl.haltedata.gtfs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.haltedata.gtfs.dto.GtfsQuay;
import nl.haltedata.gtfs.dto.GtfsQuayRepository;
import nl.haltedata.gtfs.dto.GtfsSpecialQuayRepository;

public class GtfsStopsWriter {
    final private static String SOURCE_FOLDER ="/home/gertjan/Downloads";
    final private static int BATCH_SIZE = 100;
    final private List<GtfsQuay> buffer = new ArrayList<>(BATCH_SIZE);
    private long count = 0;
    
    private Logger logger = LoggerFactory.getLogger(GtfsStopsWriter.class);

    private GtfsQuayRepository quayRepository;
    private GtfsSpecialQuayRepository specialQuayRepository;

    public GtfsStopsWriter(GtfsQuayRepository quayRepository, GtfsSpecialQuayRepository specialQuayRepository) {
        super();
        this.quayRepository = quayRepository;
        this.specialQuayRepository = specialQuayRepository;
    }

    public void run() {
        var operators = getQuayOperators();
        var stopsFile = new File(SOURCE_FOLDER, "stops.txt");
        var quayReader = new QuayReader(operators);
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
    
    private Map<Long, String> getQuayOperators() {
        Map<Long, String> operators = new HashMap<>();
        specialQuayRepository.findAllByOperator("De Lijn").forEach(quay -> operators.put(quay.getGtfsId(), quay.getOperator()));
        return operators;
    }
}
