package nl.haltedata.netex.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
public interface NetexRouteVariantRepository extends CrudRepository<NetexRouteVariant, Long> {

    @Override
    Optional<NetexRouteVariant> findById(Long sequenceId);
    
    List<NetexRouteVariant> findByLineRef(String lineId);

}

