package nl.haltedata.analysis.dto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RouteMatchRepository extends CrudRepository<RouteMatch, Long> {
    @Override
    Optional<RouteMatch> findById(Long id);

    @EntityGraph(value = "routeMatch-with-issues")
    List<RouteMatch> findByLineMatch(LineMatch lineMatch);

    @Query("""
SELECT rm
FROM RouteMatch rm
WHERE rm.lineMatch.id = :id
""")
    List<RouteMatch> findByLineMatchId(Long id);

    @Query(value = """
SELECT DISTINCT RouteMatch
FROM RouteMatch rm
JOIN FETCH LineMatch lm ON lm = rm.lineMatch
WHERE lm IN(:lineMatches)
""")
    List<RouteMatch> findByLineMatches(@Param("lineMatches") List<LineMatch> lineMatches);

    List<RouteMatch> findByAdministrativeZone(String administrativeZone);
    
    @Override
    Set<RouteMatch> findAll();

    @Modifying
    @Query(nativeQuery = true, value = """
UPDATE route_match
SET issue_stats = NULL;
WITH stats AS (
  SELECT sub.route_match_id, JSON_AGG(json_build_object(sub.severity,sub.count)) AS issue_stats
  FROM (
    SELECT rm.id AS route_match_id, rid.severity, COUNT(*)
    FROM public.route_issue_data rid
    JOIN route_match rm ON rm.id = rid.route_match_id
    GROUP BY rm.id, severity
    ORDER BY severity) AS sub
  GROUP BY sub.route_match_id)
UPDATE route_match rm
SET issue_stats = stats.issue_stats
FROM stats
WHERE stats.route_match_id = rm.id;
""")
    void updateIssueStatistics();

}
