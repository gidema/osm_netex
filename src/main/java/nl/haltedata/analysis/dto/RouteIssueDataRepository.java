package nl.haltedata.analysis.dto;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RouteIssueDataRepository extends CrudRepository<RouteIssueData, Long> {
    List<RouteIssueData> findByRouteMatchId(Long routeMatchId);
    
    @Modifying
    @Query(value = """
DELETE
FROM public.route_issue_data issue
WHERE route_match_id IN (
  SELECT rm.id
  FROM route_match rm
  JOIN line_match lm ON lm.id = rm.line_id
  WHERE lm.administrative_zone = :administrativeZone)
""", nativeQuery = true)
    void deleteByAdministrativeZone(@Param("administrativeZone") String administrativeZone);
    
    /**
     * Select issues by route matches.
     */
    @Query(value = """
SELECT DISTINCT RouteIssueData
FROM RouteIssueData rid
JOIN FETCH RouteMatch rm
WHERE rm IN(:routeMatches)
""")
    List<RouteIssueData> findByRouteMatches(@Param("routeMatches") List<RouteMatch> routeMatches);
    
    /**
     * Select issues by line.
     */
    @Query(value = """
SELECT DISTINCT RouteIssueData
FROM RouteIssueData rid, RouteMatch rm, LineMatch lm
  WHERE rm.id = :lineId
""")
    List<RouteIssueData> findByLineId(@Param("lineId") Long lineId);
    
    /**
     * Select issues by administrative zone.
     */
    @Query(value = """
SELECT DISTINCT RouteIssueData
FROM RouteIssueData rid
WHERE routeMatch IN (
  SELECT RouteMatch
  FROM RouteMatch rm
    JOIN LineMatch lm ON lm = rm.lineMatch
  WHERE lm.networkMatch.administrativeZone = :administrativeZone)
""")
    List<RouteIssueData> findByAdministrativeZone(@Param("administrativeZone") String administrativeZone);

    /**
     * Delete obsolete issue from the table.
     * Obsolete issues have old route_match id's that no longer exist.
     */
    @Modifying
    @Query(value = """
DELETE FROM public.route_issue_data issue
WHERE issue.route_match_id NOT IN (
  SELECT id FROM route_match)
""", nativeQuery = true)
    void deleteObsolete();
}
