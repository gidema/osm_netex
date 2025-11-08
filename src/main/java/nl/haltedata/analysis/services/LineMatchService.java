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
import nl.haltedata.analysis.dto.RouteIssueData;
import nl.haltedata.analysis.dto.RouteIssueDataDto;
import nl.haltedata.analysis.dto.RouteIssueDataRepository;
import nl.haltedata.analysis.dto.RouteMatch;
import nl.haltedata.analysis.dto.RouteMatchDto;
import nl.haltedata.analysis.dto.RouteMatchRepository;
import nl.haltedata.netex.dto.NetexLine;
import nl.haltedata.netex.dto.NetexLineDto;
import nl.haltedata.netex.dto.NetexRouteVariant;
import nl.haltedata.netex.dto.NetexRouteVariantDto;
import nl.haltedata.osm.dto.OsmLine;
import nl.haltedata.osm.dto.OsmLineDto;
import nl.haltedata.osm.dto.OsmRoute;
import nl.haltedata.osm.dto.OsmRouteDto;
import nl.haltedata.osm.dto.OsmRouteRepository;

@Service
public class LineMatchService implements InitializingBean {
    private static final ModelMapper singleMapper = createSingleMapper();
    private static final ModelMapper issuesMapper = createIssuesMapper();

    @Inject private LineMatchRepository lineMatchRepository;
    @Inject private RouteMatchRepository routeMatchRepository;
    @Inject private OsmRouteRepository osmRouteRepository;
    @Inject private RouteIssueDataRepository routeIssueDataRepository;

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
    
    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public Optional<LineMatchDto> findIssues(Long id) {
        var issues = routeIssueDataRepository.findByLineMatchId(id);
        var osmRoutes = osmRouteRepository.findByLineMatchId(id);
        var routeMatches = routeMatchRepository.findByLineMatchId(id);
        return lineMatchRepository.findById(id).map(lineMatch -> {
            var dto = issuesMapper.map(lineMatch, LineMatchDto.class);
            dto.setNetwork(lineMatch.getNetworkMatch().getName());
            return dto;
        });
    }

    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public List<LineMatchDto> findByAdministrativeZone(String administrativeZone) {
        return lineMatchRepository.findByAdministrativeZoneOrderByLineSort(administrativeZone).stream()
            .map(lineMatch -> modelMapper.map(lineMatch, LineMatchDto.class, "withoutRoutes"))
            .collect(Collectors.toList());
    }

    @Transactional // Important: perform within a transaction
    public void updateIssueStatistics() {
        lineMatchRepository.updateIssueStatistics();
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
    
    private static ModelMapper createIssuesMapper() {
        var mapper = new ModelMapper();
        mapper.emptyTypeMap(RouteIssueData.class, RouteIssueDataDto.class)
            .addMappings(r -> r.skip(RouteIssueDataDto::setRouteMatch))
            .implicitMappings();
        mapper.emptyTypeMap(OsmRoute.class, OsmRouteDto.class)
            .addMappings(r -> r.skip(OsmRouteDto::setOsmLine))
            .addMappings(r -> r.skip(OsmRouteDto::setQuays))
            .implicitMappings();
        mapper.emptyTypeMap(RouteMatch.class, RouteMatchDto.class)
            .addMappings(r -> r.skip(RouteMatchDto::setNetexVariant))
            .addMappings(r -> r.skip(RouteMatchDto::setLineMatch))
            .implicitMappings();
        mapper.emptyTypeMap(LineMatch.class, LineMatchDto.class)
            .addMappings(r -> r.skip(LineMatchDto::setNetexLine))
            .addMappings(r -> r.skip(LineMatchDto::setOsmLine))
            .implicitMappings();
         return mapper;
    }
}
