package nl.haltedata.analysis.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RouteMatchJosmRepository extends CrudRepository<RouteMatchJosm, RouteMatchId> {
    List<RouteMatchJosm> findByOsmRouteId(Long osmRouteId);
}
