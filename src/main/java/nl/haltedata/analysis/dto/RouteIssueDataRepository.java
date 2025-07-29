package nl.haltedata.analysis.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RouteIssueDataRepository extends CrudRepository<RouteIssueData, Long> {
    List<RouteMatch> findByRouteMatchId(Long routeMatchId);
    @Transactional
    void deleteAllByRouteMatchId(Long routeMatchId);
}
