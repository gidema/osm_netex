package nl.haltedata.netex.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;
public interface NetexRouteVariantRepository extends CrudRepository<NetexRouteVariant, Long> {

    @Override
    @EntityGraph(value = "netexRouteVariant", type=EntityGraphType.FETCH)
    Iterable<NetexRouteVariant> findAll();
    
    @Override
    Optional<NetexRouteVariant> findById(Long id);
    
    @Override
    @EntityGraph(value = "netexRouteVariant", type=EntityGraphType.LOAD)
    Iterable<NetexRouteVariant> findAllById(Iterable<Long> ids);

}

