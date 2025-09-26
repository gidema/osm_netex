package nl.haltedata.analysis.dto;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RouteIssueDataRepository extends CrudRepository<RouteIssueData, RouteMatchId> {
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
