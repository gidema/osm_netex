package nl.haltedata.osm.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;

public interface OsmNetworkRepository extends CrudRepository<OsmNetwork, Long> {
    @Override
    @EntityGraph(value = "osmNetworkWithLines", type=EntityGraphType.FETCH)
    Optional<OsmNetwork> findById(Long id);
    //
}

