package nl.haltedata.chb;

import java.util.LinkedList;
import java.util.List;

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

@SpringBootApplication
public class HalteDataApplication {
    private List<StopPlaceDto> stopplaceDtos = new LinkedList<>();
    private List<QuayDto> quayDtos = new LinkedList<>();

    public static void main(String[] args) {
        SpringApplication.run(HalteDataApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StopplaceRepository stopplaceRepository, 
            QuayRepository quayRepository) {
        return args -> {
            var dataReader = new ChbDataReader();
            var chbData = dataReader.readChbData("/home/gertjan/Downloads/ExportCHB20240102013106.xml");
            chbData.getStopplaces().forEach(this::handleStopPlaces);
            stopplaceRepository.saveAll(stopplaceDtos);
            quayRepository.saveAll(quayDtos);
        };
    }

    private void handleStopPlaces(Stopplaces stopPlaces) {
        stopPlaces.getStopplace().forEach(this::handleStopplace);
    }

    private void handleStopplace(Stopplace stopPlace) {
        stopplaceDtos.add(mapStopPlace(stopPlace));
        stopPlace.getQuays().getQuay().forEach(quay -> handleQuay(quay, stopPlace));
    }

    private void handleQuay(Quay quay, Stopplace stopPlace) {
        QuayDto dto = mapQuay(quay, stopPlace);
        quayDtos.add(dto);
    }
    
    private StopPlaceDto mapStopPlace(Stopplace stopplace) {
        var dto = new StopPlaceDto();

        dto.setId(stopplace.getID());
        dto.setValidfrom(stopplace.getValidfrom().toGregorianCalendar());
        dto.setStopplacecode(stopplace.getStopplacecode());
        dto.setStopplacetype(stopplace.getStopplacetype());
        dto.setPublicname(stopplace.getStopplacename().getPublicname());
        dto.setTown(stopplace.getStopplacename().getTown());
        dto.setPublicnamemedium(stopplace.getStopplacename().getPublicnamemedium());
        dto.setPublicnamelong(stopplace.getStopplacename().getPublicnamelong());
        dto.setDescription(stopplace.getStopplacename().getDescription());
        dto.setStopplaceindication(stopplace.getStopplacename().getStopplaceindication());
        dto.setStreet(stopplace.getStopplacename().getStreet());
        dto.setStopplacestatus(stopplace.getStopplacestatusdata().getStopplacestatus());
        dto.setMutationdate(stopplace.getMutationdate().toGregorianCalendar());
        dto.setUiccode(stopplace.getUiccode());
        dto.setInternalname(stopplace.getInternalname());
        dto.setStopplaceowner(stopplace.getStopplaceowner().getStopplaceownercode());
        dto.setPlacecode(stopplace.getPlacecode());
        return dto;
    }
    
    private QuayDto mapQuay(Quay quay, Stopplace stopPlace) {
        var quayDto = new QuayDto();
        quayDto.setID(quay.getID());
        quayDto.setStopPlaceId(stopPlace.getID());
        quayDto.setMutationdate(quay.getMutationdate().toGregorianCalendar());
        quayDto.setOnlygetout(quay.isOnlygetout());
        quayDto.setQuaycode(quay.getQuaycode());
        quayDto.setValidfrom(quay.getValidfrom().toGregorianCalendar());
        return quayDto;
    }
}
