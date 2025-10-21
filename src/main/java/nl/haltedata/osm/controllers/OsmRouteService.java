package nl.haltedata.osm.controllers;

import java.util.Collections;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmRoute;
import nl.haltedata.osm.dto.OsmRouteDto;
import nl.haltedata.osm.dto.OsmRouteRepository;

@Service
public class OsmRouteService implements InitializingBean {
    @Inject
    private OsmRouteRepository osmRouteRepository;

    @Inject
    private ModelMapper modelMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        modelMapper.createTypeMap(OsmRoute.class, OsmRouteDto.class, "withQuays");
        modelMapper.createTypeMap(OsmRoute.class, OsmRouteDto.class)
            .addMappings(mapper -> mapper.skip(OsmRouteDto::setQuays));
    }

    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public Optional<OsmRouteDto> findById(Long id) {
        return osmRouteRepository.findById(id).map(osmRoute -> {
            return modelMapper.map(osmRoute, OsmRouteDto.class, "withQuays");
        });
    }
}
