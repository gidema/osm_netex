package nl.haltedata.analysis.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RouteMatchRepository extends CrudRepository<RouteMatch, Long> {
    List<RouteMatch> findByLineId(Long lineId);

    List<RouteMatch> findByOsmRouteId(Long osmRouteId);

    List<RouteMatch> findByNetwork(String network);
}
