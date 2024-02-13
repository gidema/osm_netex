package nl.haltedata.gtfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import nl.haltedata.gtfs.dto.GtfsQuayRepository;
import nl.haltedata.gtfs.dto.GtfsStopTimeRepository;

@SpringBootApplication
public class GtsfImportApplication {
    private Logger logger = LoggerFactory.getLogger(GtsfImportApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GtsfImportApplication.class, args);
    }

    @Bean
    CommandLineRunner processStops(GtfsQuayRepository repository) {
        GtfsStopsWriter stopswriter = new GtfsStopsWriter(repository);
        return args -> {
            stopswriter.run();
        };
    }
    
//    @Bean
//    CommandLineRunner processStopTimes(GtfsStopTimeRepository repository) {
//        GtfsStopTimesWriter stWriter = new GtfsStopTimesWriter(repository);
//        return args -> {
//            stWriter.run();
//        };
//    }

}
