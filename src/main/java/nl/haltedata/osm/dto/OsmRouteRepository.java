package nl.haltedata.osm.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;

public interface OsmRouteRepository extends CrudRepository<OsmRoute, Long> {

    @Override
    @EntityGraph(value = "osmRouteWithQuays", type=EntityGraphType.FETCH)
    Iterable<OsmRoute> findAll();

    List<OsmRoute> findByOsmLineId(Long osmLineId);
    
    @Override
    @EntityGraph(value = "osmRouteWithQuays", type=EntityGraphType.FETCH)
    Optional<OsmRoute> findById(Long id);
    
    @Override
    @EntityGraph(value = "osmRouteWithQuays", type=EntityGraphType.FETCH)
    Iterable<OsmRoute> findAllById(Iterable<Long> ids);
}

