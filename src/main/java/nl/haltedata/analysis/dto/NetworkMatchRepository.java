package nl.haltedata.analysis.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

public interface NetworkMatchRepository extends CrudRepository<NetworkMatch, String> {

    @EntityGraph(value = "networkMatch-list", type=EntityGraphType.FETCH)
    public List<NetworkMatch> findAllByOrderByName();

    public Optional<NetworkMatch> findByAdministrativeZone(String administrativeZone);

    @Modifying
    @Query(nativeQuery = true, value = """
    UPDATE network_match
    SET issue_stats = NULL;
    WITH stats AS (
      SELECT sub.administrative_zone, JSON_AGG(json_build_object(sub.severity,sub.count)) AS issue_stats
      FROM (
        SELECT nm.administrative_zone, rid.severity, COUNT(*)
        FROM public.route_issue_data rid
        JOIN route_match rm ON rm.id = rid.route_match_id
        JOIN line_match lm ON lm.id = rm.line_id
        JOIN network_match nm ON nm.administrative_zone = lm.administrative_zone
        GROUP BY nm.administrative_zone, severity
        ORDER BY severity) AS sub
      GROUP BY sub.administrative_zone)
    UPDATE network_match nm
    SET issue_stats = stats.issue_stats
    FROM stats
    WHERE stats.administrative_zone = nm.administrative_zone;
    """)
    void updateIssueStatistics();
}
