package nl.haltedata.netex.controllers;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.netex.dto.NetexLine;
import nl.haltedata.netex.dto.NetexLineDto;
import nl.haltedata.netex.dto.NetexNetwork;
import nl.haltedata.netex.dto.NetexNetworkDto;
import nl.haltedata.netex.dto.NetexNetworkRepository;

@RestController
public class NetexNetworkService {
    private static ModelMapper multipleNetworkMapper = createMultipleNetworkMapper();
    
    @Inject
    private NetexNetworkRepository repository;
    
    /**
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public Optional<NetexNetworkDto> findByAdministrativeZone(String administrativeZone) throws Exception {
        return repository.findByAdministrativeZone(administrativeZone).map(network -> {
            return multipleNetworkMapper.map(network, NetexNetworkDto.class, "withLines");
        });
    }
    
    private static ModelMapper createMultipleNetworkMapper() {
        var mapper = new ModelMapper();
        mapper.emptyTypeMap(NetexLine.class, NetexLineDto.class)
            .addMappings(m -> m.skip(NetexLineDto::setRouteVariants))
            .addMappings(m -> m.skip(NetexLineDto::setNetexNetwork))
            .implicitMappings();
        mapper.createTypeMap(NetexNetwork.class, NetexNetworkDto.class);
        return mapper;
    }
}