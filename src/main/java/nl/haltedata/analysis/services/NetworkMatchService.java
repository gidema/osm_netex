package nl.haltedata.analysis.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.LineMatch;
import nl.haltedata.analysis.dto.LineMatchDto;
import nl.haltedata.analysis.dto.NetworkMatch;
import nl.haltedata.analysis.dto.NetworkMatchDto;
import nl.haltedata.analysis.dto.NetworkMatchRepository;
import nl.haltedata.netex.dto.NetexLine;
import nl.haltedata.netex.dto.NetexLineDto;
import nl.haltedata.netex.dto.NetexNetwork;
import nl.haltedata.netex.dto.NetexNetworkDto;
import nl.haltedata.osm.dto.OsmLine;
import nl.haltedata.osm.dto.OsmLineDto;
import nl.haltedata.osm.dto.OsmNetwork;
import nl.haltedata.osm.dto.OsmNetworkDto;

@Service
public class NetworkMatchService {
    @Inject
    private NetworkMatchRepository networkMatchRepository;

    private static final ModelMapper singleNetworkMapper = createSingleNetworkMapper();
    private static final ModelMapper multipleNetworkMapper = createMultipleNetworkMapper();
    
    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public Optional<NetworkMatchDto> findById(String id) {
        return networkMatchRepository.findById(id).map(networkMatch -> {
            var match = singleNetworkMapper.map(networkMatch, NetworkMatchDto.class);
            return match;
        });
    }

    @SuppressWarnings({ "exports"})
    @Transactional(readOnly = true) // Important: perform within a transaction
    public List<NetworkMatchDto> findAllByOrderByName() {
        return networkMatchRepository.findAllByOrderByName().stream()
            .map(networkMatch -> {
                var match = multipleNetworkMapper.map(networkMatch, NetworkMatchDto.class);
                return match;
            })
            .collect(Collectors.toList());
    }
    
    private static ModelMapper createSingleNetworkMapper() {
        var mapper = new ModelMapper();
        mapper.emptyTypeMap(OsmNetwork.class, OsmNetworkDto.class)
            .addMappings(m -> m.skip(OsmNetworkDto::setLines))
            .implicitMappings();
        mapper.emptyTypeMap(OsmLine.class, OsmLineDto.class)
            .addMappings(m -> m.skip(OsmLineDto::setOsmNetworks))
            .addMappings(m -> m.skip(OsmLineDto::setRoutes))
            .implicitMappings();
        mapper.emptyTypeMap(NetexLine.class, NetexLineDto.class)
            .addMappings(m -> m.skip(NetexLineDto::setNetexNetwork))
            .addMappings(m -> m.skip(NetexLineDto::setRouteVariants))
            .implicitMappings();
        mapper.emptyTypeMap(NetexNetwork.class, NetexNetworkDto.class)
            .addMappings(m -> m.skip(NetexNetworkDto::setLines))
            .implicitMappings();
        mapper.emptyTypeMap(LineMatch.class, LineMatchDto.class)
            .addMappings(m -> m.skip(LineMatchDto::setNetwork))
            .addMappings(m -> m.skip(LineMatchDto::setRouteMatches))
            .implicitMappings();
        mapper.emptyTypeMap(NetworkMatch.class, NetworkMatchDto.class)
            .implicitMappings();
         return mapper;
    }

    private static ModelMapper createMultipleNetworkMapper() {
        var mapper = new ModelMapper();
        mapper.emptyTypeMap(NetexNetwork.class, NetexNetworkDto.class)
            .addMappings(m -> m.skip(NetexNetworkDto::setLines))
            .implicitMappings();
        mapper.emptyTypeMap(OsmNetwork.class, OsmNetworkDto.class)
            .addMappings(m -> m.skip(OsmNetworkDto::setLines))
            .implicitMappings();
        mapper.emptyTypeMap(NetworkMatch.class, NetworkMatchDto.class)
            .addMappings(n -> n.skip(NetworkMatchDto::setLineMatches))
            .implicitMappings();
         return mapper;
    }
}
