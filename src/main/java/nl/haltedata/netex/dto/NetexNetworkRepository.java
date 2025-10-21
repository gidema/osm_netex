package nl.haltedata.netex.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;

public interface NetexNetworkRepository extends CrudRepository<NetexNetwork, String> {

    @EntityGraph(value = "netexNetworkWithLines", type=EntityGraphType.FETCH)
    public Optional<NetexNetwork> findByAdministrativeZone(String administrativeZone);
    
    public Iterable<NetexNetwork> findAllByOrderByName();
}

