package nl.haltedata.analysis.dto;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RouteIssueDataRepository extends CrudRepository<RouteIssueData, RouteMatchId> {
    List<RouteIssueData> findByRouteMatchId(Long routeMatchId);
    
    @Modifying
    @Query("""
    DELETE
    FROM public.route_issue_data issue
    JOIN route_match rm ON issue.route_match_id = rm.id
    JOIN line_match lm ON lm.id = rm.line_id
    WHERE lm.network = :network
""")
    void deleteByNetwork(@Param("title") String title);
    
    /**
     * Delete obsolete issue from the table.
     * Obsolete issues have old route_match id's that no longer exist.
     */
    @Modifying
    @Query("""
DELETE FROM public.route_issue_data issue
WHERE issue.route_match_id NOT IN (
  SELECT id FROM route_match)
""")
    void deleteObsolete();
}
