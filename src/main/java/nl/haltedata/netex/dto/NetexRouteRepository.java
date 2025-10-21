package nl.haltedata.netex.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
public interface NetexRouteRepository extends CrudRepository<NetexRoute, String> {

    @Override
    Optional<NetexRoute> findById(String routeId);

    List<NetexRoute> findByLineRef(String lineRef);

}

