package nl.haltedata.osm.dto;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OsmRouteIssueRepository extends CrudRepository<OsmRouteIssue, Long> {

    @Modifying
    @Query(nativeQuery = true, value="""
DELETE FROM osm_pt.osm_route_issue WHERE issue_type = 'RouteInMultipleLines';
INSERT INTO osm_pt.osm_route_issue (route_id, issue_type, severity)
SELECT osm_route_id, 'RouteInMultipleLines', 'major'
FROM osm_pt.osm_route_master_route
GROUP BY osm_route_id
HAVING count(*) > 1;
""")
    void updateRouteInMultipleLinesIssue();

    @Modifying
    @Query(nativeQuery = true, value="""
DELETE FROM osm_pt.osm_route_issue WHERE issue_type = 'RouteNotInLine';
INSERT INTO osm_pt.osm_route_issue (route_id, issue_type, severity)
SELECT r.osm_route_id, 'RouteNotInLine', 'major'
FROM osm_pt.osm_route r
LEFT JOIN osm_pt.osm_route_master_route rmr ON rmr.osm_route_id = r.osm_route_id
WHERE rmr.osm_route_id IS NULL AND r.transport_mode='bus';
""")
    void updateRouteNotInLineIssue();
    
    @Modifying
    @Query(nativeQuery = true, value="""
DELETE FROM osm_pt.osm_route_issue WHERE issue_type = 'RouteRefDiffersFromLineRef';
INSERT INTO osm_pt.osm_route_issue (route_id, issue_type, severity, parameters)
SELECT r.osm_route_id, 'RouteRefDiffersFromLineRef', 'major', ARRAY[ol.line_number, r.route_ref]
FROM osm_pt.osm_route r
JOIN osm_pt.osm_route_master_route rmr ON rmr.osm_route_id = r.osm_route_id
JOIN osm_pt.osm_line ol ON ol.id = rmr.osm_route_master_id
WHERE NOT ol.line_number = r.route_ref;
""")
    void updateRouteRefDiffersFromLineRefIssue();

}
