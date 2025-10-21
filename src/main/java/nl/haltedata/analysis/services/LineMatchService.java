package nl.haltedata.analysis.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.LineMatch;
import nl.haltedata.analysis.dto.LineMatchDto;
import nl.haltedata.analysis.dto.LineMatchRepository;
import nl.haltedata.analysis.dto.NetworkMatch;
import nl.haltedata.analysis.dto.NetworkMatchDto;
import nl.haltedata.analysis.dto.RouteMatch;
import nl.haltedata.analysis.dto.RouteMatchDto;
import nl.haltedata.netex.dto.NetexLine;
import nl.haltedata.netex.dto.NetexLineDto;
import nl.haltedata.netex.dto.NetexNetwork;
import nl.haltedata.netex.dto.NetexNetworkDto;
import nl.haltedata.netex.dto.NetexRouteVariant;
import nl.haltedata.netex.dto.NetexRouteVariantDto;
import nl.haltedata.osm.dto.OsmLine;
import nl.haltedata.osm.dto.OsmLineDto;
import nl.haltedata.osm.dto.OsmNetwork;
import nl.haltedata.osm.dto.OsmNetworkDto;
import nl.haltedata.osm.dto.OsmRoute;
import nl.haltedata.osm.dto.OsmRouteDto;

@Service
public class LineMatchService implements InitializingBean {
    @Inject
    private LineMatchRepository lineMatchRepository;

    private static final ModelMapper singleMapper = createSingleMapper();

    @Inject
    private ModelMapper modelMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        modelMapper.createTypeMap(LineMatch.class, LineMatchDto.class, "withRoutes");
        modelMapper.createTypeMap(LineMatch.class, LineMatchDto.class)
            .addMappings(mapper -> mapper.skip(LineMatchDto::setRouteMatches));
    }
    
    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public Optional<LineMatchDto> findById(Long id) {
        return lineMatchRepository.findById(id).map(lineMatch -> {
            var dto = singleMapper.map(lineMatch, LineMatchDto.class);
            dto.setNetwork(lineMatch.getNetworkMatch().getName());
            return dto;
        });
    }

//    @Transactional(readOnly = true) // Important: perform within a transaction
//    public List<LineMatchDto> findByAdministrativeZone(String administrativeZone) {
//        Optional<LineMatch> LineMatch = LineMatchRepository.;
//        // TODO Auto-generated method stub
//        return null;
//    }

    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public List<LineMatchDto> findByAdministrativeZone(String administrativeZone) {
        return lineMatchRepository.findByAdministrativeZoneOrderByLineSort(administrativeZone).stream()
            .map(lineMatch -> modelMapper.map(lineMatch, LineMatchDto.class, "withoutRoutes"))
            .collect(Collectors.toList());
    }
    
    private static ModelMapper createSingleMapper() {
        var mapper = new ModelMapper();
        mapper.emptyTypeMap(OsmLine.class, OsmLineDto.class)
            .addMappings(m -> m.skip(OsmLineDto::setOsmNetworks))
            .addMappings(m -> m.skip(OsmLineDto::setRoutes))
            .implicitMappings();
        mapper.emptyTypeMap(NetexLine.class, NetexLineDto.class)
            .addMappings(m -> m.skip(NetexLineDto::setNetexNetwork))
            .addMappings(m -> m.skip(NetexLineDto::setRouteVariants))
            .implicitMappings();
        mapper.emptyTypeMap(OsmRoute.class, OsmRouteDto.class)
            .addMappings(r -> r.skip(OsmRouteDto::setOsmLine))
            .addMappings(r -> r.skip(OsmRouteDto::setQuays))
            .implicitMappings();
        mapper.emptyTypeMap(NetexRouteVariant.class, NetexRouteVariantDto.class)
            .addMappings(r -> r.skip(NetexRouteVariantDto::setQuays))
            .implicitMappings();
        mapper.emptyTypeMap(RouteMatch.class, RouteMatchDto.class)
            .addMappings(r -> r.skip(RouteMatchDto::setIssues))
            .implicitMappings();
        mapper.emptyTypeMap(LineMatch.class, LineMatchDto.class)
            .implicitMappings();
         return mapper;
    }
}
