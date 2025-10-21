package nl.haltedata.analysis.services;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import nl.haltedata.analysis.dto.LineMatch;
import nl.haltedata.analysis.dto.LineMatchDto;
import nl.haltedata.analysis.dto.NetworkMatchDto;
import nl.haltedata.analysis.dto.RouteMatch;
import nl.haltedata.analysis.dto.RouteMatchDto;
import nl.haltedata.analysis.dto.RouteMatchRepository;
import nl.haltedata.netex.dto.NetexLine;
import nl.haltedata.netex.dto.NetexLineDto;
import nl.haltedata.netex.dto.NetexNetworkRepository;
import nl.haltedata.netex.dto.NetexRouteVariant;
import nl.haltedata.netex.dto.NetexRouteVariantDto;
import nl.haltedata.netex.dto.NetexRouteVariantRepository;
import nl.haltedata.osm.dto.OsmLine;
import nl.haltedata.osm.dto.OsmRoute;
import nl.haltedata.osm.dto.OsmRouteDto;
import nl.haltedata.osm.dto.OsmRouteRepository;

@Service
public class RouteMatchService implements InitializingBean {
    @Inject
    private RouteMatchRepository routeMatchRepository;

    @Inject
    private NetexNetworkRepository netexNetworkRepository;

    @Inject
    private NetexRouteVariantRepository routeVariantRepository;

    @Inject
    private EntityManager entityManager;

    @Inject
    private OsmRouteRepository osmRouteRepository;

    @Inject
    private ModelMapper modelMapper;

    private ModelMapper analysisMapper = createAnalysisMapper();

    @Override
    public void afterPropertiesSet() {
        modelMapper.createTypeMap(RouteMatch.class, RouteMatchDto.class);
    }

    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public Optional<RouteMatchDto> findById(Long routeMatchId) {
        return routeMatchRepository.findById(routeMatchId).map(routeMatch -> {
            return modelMapper.map(routeMatch, RouteMatchDto.class);
        });
    }

//    @Transactional(readOnly = true) // Important: perform within a transaction
//    public List<RouteMatchDto> findByAdministrativeZone(String administrativeZone) {
//        Optional<RouteMatch> routeMatch = routeMatchRepository.;
//        // TODO Auto-generated method stub
//        return null;
//    }

    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public List<RouteMatchDto> findByLineMatchId(Long id) {
        return modelMapper.map(routeMatchRepository.findByLineMatchId(id), List.class);
    }

    @Transactional(readOnly = true) // Important: perform within a transaction
    public List<RouteMatchDto> findAll() {
        var netexNetworks = netexNetworkRepository.findAll();
        var osmRoutes = osmRouteRepository.findAll();
        var variants = routeVariantRepository.findAll();
//        var variants = routeVariantRepository.findAll();
        // TODO The following line is a hack
        variants.forEach(v -> v.setNetexRoutes(Collections.emptyList()));
        var routeMatches = routeMatchRepository.findAll();
        var rm = routeMatchRepository.findById(1280265L);
        var dto = routeMatches.stream().map(routeMatch -> {
            var id = routeMatch.getId();
            return analysisMapper.map(routeMatch, RouteMatchDto.class);
        }).toList();
        return dto;
    }

    private ModelMapper createAnalysisMapper() {
        var mapper = new ModelMapper();
        mapper.emptyTypeMap(NetexLine.class, NetexLineDto.class)
            .addMappings(m -> m.skip(NetexLineDto::setNetexNetwork))
            .implicitMappings();
        mapper.createTypeMap(NetexRouteVariant.class, NetexRouteVariantDto.class);
        mapper.emptyTypeMap(LineMatch.class, LineMatchDto.class)
            .addMappings(m -> m.skip(LineMatchDto::setOsmLine))
            .addMappings(m -> m.skip(LineMatchDto::setNetexLine))
            .addMappings(m -> m.skip(LineMatchDto::setRouteMatches))
            .implicitMappings();
        mapper.emptyTypeMap(OsmRoute.class, OsmRouteDto.class)
            .addMappings(m -> m.skip(OsmRouteDto::setOsmLine))
            .implicitMappings();
        mapper.emptyTypeMap(RouteMatch.class, RouteMatchDto.class)
            .addMappings(m -> m.skip(RouteMatchDto::setIssues))
            .implicitMappings();
        return mapper;
    }
}
