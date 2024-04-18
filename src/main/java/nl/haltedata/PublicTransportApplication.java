package nl.haltedata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = { "nl.haltedata.chb", "nl.haltedata.gtfs", "nl.haltedata.netex" },
    excludeFilters = { @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.gtfs.config.batch.*"),
        @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.netex.config.batch.*"),
        @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.chb.config.batch.*")})
public class PublicTransportApplication {
    private Logger logger = LoggerFactory.getLogger(PublicTransportApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PublicTransportApplication.class, args);
    }

//    @Bean
//    CommandLineRunner processStops(GtfsQuayRepository repository, GtfsSpecialQuayRepository specialQuayRepository) {
//        GtfsStopsWriter stopswriter = new GtfsStopsWriter(repository, specialQuayRepository);
//        return args -> {
//            stopswriter.run();
//        };
//    }
    
//    @Bean
//    CommandLineRunner processStopTimes(GtfsStopTimeRepository repository) {
//        GtfsStopTimesWriter stWriter = new GtfsStopTimesWriter(repository);
//        return args -> {
//            stWriter.run();
//        };
//    }
}
