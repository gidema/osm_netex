package nl.haltedata.netex.controllers;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.LineMatch;
import nl.haltedata.analysis.dto.LineMatchDto;
import nl.haltedata.analysis.dto.RouteMatch;
import nl.haltedata.analysis.dto.RouteMatchDto;
import nl.haltedata.netex.dto.NetexLine;
import nl.haltedata.netex.dto.NetexLineDto;
import nl.haltedata.netex.dto.NetexLineRepository;
import nl.haltedata.netex.dto.NetexNetwork;
import nl.haltedata.netex.dto.NetexNetworkDto;
import nl.haltedata.netex.dto.NetexRouteVariant;
import nl.haltedata.netex.dto.NetexRouteVariantDto;
import nl.haltedata.osm.dto.OsmLine;
import nl.haltedata.osm.dto.OsmLineDto;
import nl.haltedata.osm.dto.OsmRoute;
import nl.haltedata.osm.dto.OsmRouteDto;

@RestController
public class NetexLineService implements InitializingBean {

    private final static ModelMapper singleLineMapper = createSingleLineMapper();
    

    @Inject
    ModelMapper modelMapper;
    
    @Inject
    private NetexLineRepository repository;


    @Override
    public void afterPropertiesSet() throws Exception {
//        modelMapper.createTypeMap(NetexLine.class, NetexLineDto.class, "withRouteVariants");
        modelMapper.createTypeMap(NetexLine.class, NetexLineDto.class)
            .addMappings(mapper -> mapper.skip(NetexLineDto::setRouteVariants));
    }
    
    /**
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public Optional<NetexLineDto> findById(String id) throws Exception {
        return repository.findById(id).map(line -> {
            return singleLineMapper.map(line, NetexLineDto.class);
        });
    }

    private static ModelMapper createSingleLineMapper() {
        var mapper = new ModelMapper();
        mapper.emptyTypeMap(NetexNetwork.class, NetexNetworkDto.class)
            .addMappings(r -> r.skip(NetexNetworkDto::setLines))
            .implicitMappings();
        mapper.emptyTypeMap(NetexRouteVariant.class, NetexRouteVariantDto.class)
            .addMappings(r -> r.skip(NetexRouteVariantDto::setQuays))
            .implicitMappings();
        mapper.emptyTypeMap(NetexLine.class, NetexLineDto.class)
            .implicitMappings();
         return mapper;
    }

}