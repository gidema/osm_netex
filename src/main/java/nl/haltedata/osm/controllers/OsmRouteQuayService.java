package nl.haltedata.osm.controllers;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmRouteQuay;
import nl.haltedata.osm.dto.OsmRouteQuayDto;
import nl.haltedata.osm.dto.OsmRouteQuayRepository;

@Service
public class OsmRouteQuayService implements InitializingBean {
    @Inject
    private OsmRouteQuayRepository osmRouteQuayRepository;

    @Inject
    private ModelMapper modelMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        modelMapper.createTypeMap(OsmRouteQuay.class, OsmRouteQuayDto.class);
    }

    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public Optional<OsmRouteQuayDto> findById(Long id) {
        return osmRouteQuayRepository.findById(id).map(osmRouteQuay -> {
            return modelMapper.map(osmRouteQuay, OsmRouteQuayDto.class);
        });
    }
}
