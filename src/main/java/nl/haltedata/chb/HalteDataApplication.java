package nl.haltedata.chb;

import java.time.Instant;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import nl.chb.Quay;
import nl.chb.Stopplace;
import nl.chb.Stopplaces;
import nl.haltedata.chb.dto.QuayDto;
import nl.haltedata.chb.dto.QuayRepository;
import nl.haltedata.chb.dto.StopPlaceDto;
import nl.haltedata.chb.dto.StopplaceRepository;
import nl.haltedata.chb.mapping.QuayMapper;
import nl.haltedata.chb.mapping.StopPlaceMapper;

@SpringBootApplication
public class HalteDataApplication {
        private Logger logger = LoggerFactory.getLogger(HalteDataApplication.class);

    private List<StopPlaceDto> stopplaceDtos = new LinkedList<>();
    private List<QuayDto> quayDtos = new LinkedList<>();

    private QuayMapper quayMapper = new QuayMapper();
    private StopPlaceMapper stopPlaceMapper = new StopPlaceMapper();
    
    public static void main(String[] args) {
        SpringApplication.run(HalteDataApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StopplaceRepository stopplaceRepository, 
            QuayRepository quayRepository) {
        return args -> {
            var dataReader = new ChbDataReader();
            var instant = Instant.now();
            var chbData = dataReader.readChbData("/home/gertjan/Downloads/ExportCHB20240213013231.xml");
            var stopPlaces = chbData.getStopplaces();
            logger.info("Read {} stop places from the CHB source file in {} seconds.", stopPlaces.get(0).getStopplace().size(),
                    SECONDS.between(instant, Instant.now()));
            chbData.getStopplaces().forEach(this::handleStopPlaces);
            instant = Instant.now();
            stopplaceRepository.saveAll(stopplaceDtos);
            logger.info("Saved {} stop places to the database in {} seconds.", stopplaceDtos.size(),
                    SECONDS.between(instant, Instant.now()));
            instant = Instant.now();
            quayRepository.saveAll(quayDtos);
            logger.info("Saved {} quays to the database in {} seconds.", quayDtos.size(),
                    SECONDS.between(instant, Instant.now()));
        };
    }

    private void handleStopPlaces(Stopplaces stopPlaces) {
        stopPlaces.getStopplace().forEach(this::handleStopplace);
    }

    private void handleStopplace(Stopplace stopPlace) {
        stopplaceDtos.add(stopPlaceMapper.map(stopPlace, null));
        stopPlace.getQuays().getQuay().forEach(quay -> handleQuay(quay, stopPlace));
    }

    private void handleQuay(Quay quay, Stopplace stopPlace) {
        QuayDto dto = quayMapper.map(quay, stopPlace);
        quayDtos.add(dto);
    }
}
