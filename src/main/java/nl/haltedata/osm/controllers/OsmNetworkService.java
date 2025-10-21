package nl.haltedata.osm.controllers;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmLine;
import nl.haltedata.osm.dto.OsmLineDto;
import nl.haltedata.osm.dto.OsmNetwork;
import nl.haltedata.osm.dto.OsmNetworkDto;
import nl.haltedata.osm.dto.OsmNetworkRepository;

@Service
public class OsmNetworkService {
    private static ModelMapper multipleNetworkMapper = createMultipleNetworkMapper();

    @Inject
    private OsmNetworkRepository osmNetworkRepository;

    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public Optional<OsmNetworkDto> findById(Long id) {
        return osmNetworkRepository.findById(id).map(osmNetwork -> {
            return multipleNetworkMapper.map(osmNetwork, OsmNetworkDto.class);
        });
    }
    
    private static ModelMapper createMultipleNetworkMapper() {
        var mapper = new ModelMapper();
        mapper.emptyTypeMap(OsmLine.class, OsmLineDto.class)
            .addMappings(m -> m.skip(OsmLineDto::setRoutes))
            .addMappings(m -> m.skip(OsmLineDto::setNetwork))
            .implicitMappings();
        mapper.createTypeMap(OsmNetwork.class, OsmNetworkDto.class);
        return mapper;
    }
}
