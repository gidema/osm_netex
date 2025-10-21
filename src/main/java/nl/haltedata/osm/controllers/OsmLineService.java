package nl.haltedata.osm.controllers;

import java.util.Collections;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmLine;
import nl.haltedata.osm.dto.OsmLineDto;
import nl.haltedata.osm.dto.OsmLineRepository;

@Service
public class OsmLineService implements InitializingBean {
    @Inject
    private OsmLineRepository osmLineRepository;

    @Inject
    private ModelMapper modelMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        modelMapper.createTypeMap(OsmLine.class, OsmLineDto.class, "withRoutes")
            .addMappings(mapper -> mapper.skip(OsmLineDto::setOsmNetworks));
        modelMapper.createTypeMap(OsmLine.class, OsmLineDto.class)
            .addMappings(mapper -> mapper.skip(OsmLineDto::setRoutes));
    }

    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public Optional<OsmLineDto> findById(Long id) {
        return osmLineRepository.findById(id).map(osmLine -> {
            osmLine.setOsmNetworks(Collections.emptyList());
            return modelMapper.map(osmLine, OsmLineDto.class, "withRoutes");
        });
    }

//    @SuppressWarnings("exports")
//    public List<OsmLineDto> findByOsmNetworkIdOrderByLineSort(Long id) {
//        return osmLineRepository.findByOsmNetworkIdOrderByLineSort(id).stream()
//                .map(line -> modelMapper.map(line, OsmLineDto.class))
//                .collect(Collectors.toList());
//    }
}
