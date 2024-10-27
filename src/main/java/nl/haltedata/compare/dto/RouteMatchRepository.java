package nl.haltedata.compare.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RouteMatchRepository extends CrudRepository<RouteMatch, RouteMatchId> {
    List<RouteMatch> findByOsmRouteId(Long osmRouteId);
}
