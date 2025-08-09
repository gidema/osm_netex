package nl.haltedata.analysis.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RouteIssueDataRepository extends CrudRepository<RouteIssueData, RouteMatchId> {
    List<RouteIssueData> findByRouteMatchId(Long routeMatchId);
    void deleteAllByRouteMatchId(Long routeMatchId);
}
