package nl.haltedata.netex.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
public interface DimNetexRouteRepository extends CrudRepository<DimNetexRoute, String> {

    @Override
    Optional<DimNetexRoute> findById(String routeId);

    List<DimNetexRoute> findByLineId(String lineId);

}

