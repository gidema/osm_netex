package nl.haltedata.analysis.dto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LineMatchRepository extends CrudRepository<LineMatch, Long> {
//    @Override
//    @Query("""
//      SELECT lm
//      FROM LineMatch lm
//      LEFT JOIN FETCH lm.routes AS route
//      WHERE lm.id = :id
//      ORDER BY route.directionType
//""")
    @Override
//    @EntityGraph("lineMatch-with-routeMatches-issues")
    Optional<LineMatch> findById(Long id);
    
//    List<LineMatch> findByNetwork(String network);
    @Query("""
      SELECT lm
      FROM LineMatch lm, NetworkMatch nm
      WHERE nm.administrativeZone = :administrativeZone
""")
    List<LineMatch> findByAdministrativeZoneOrderByLineSort(@Param("administrativeZone")String administrativeZone);
    
    List<LineMatch> findByIdIn(Set<Long> ids);

    @Query("""
            SELECT lm
            FROM LineMatch lm
            JOIN NetworkMatch nm ON lm.networkMatch = nm
            WHERE nm.administrativeZone = :administrativeZone
      """)
    List<LineMatch> findByAdministrativeZone(@Param("administrativeZone") String administrativeZone);

    @Modifying
    @Query(nativeQuery = true, value = """
    UPDATE line_match
    SET issue_stats = NULL;
    WITH stats AS (
      SELECT sub.line_match_id, JSON_AGG(json_build_object(sub.severity,sub.count)) AS issue_stats
      FROM (
        SELECT lm.id AS line_match_id, rid.severity, COUNT(*)
        FROM public.route_issue_data rid
        JOIN route_match rm ON rm.id = rid.route_match_id
        JOIN line_match lm ON lm.id = rm.line_id
        GROUP BY lm.id, severity
        ORDER BY severity) AS sub
      GROUP BY sub.line_match_id)
    UPDATE line_match lm
    SET issue_stats = stats.issue_stats
    FROM stats
    WHERE stats.line_match_id = lm.id;
    """)
    void updateIssueStatistics();
}
