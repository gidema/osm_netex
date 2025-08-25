package nl.haltedata.osm.dto;

import java.util.List;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

public interface OsmRouteRepository extends CrudRepository<OsmRoute, Long> {

    List<OsmRoute> findByOsmLineId(Long osmLineId);
    
    @NativeQuery("""
SELECT osmr.osm_route_id, osmr.name, osmr.network AS route_network, orm.network, orm.operator AS route_operator, osmr.operator AS network_operator
FROM osm_pt.osm_route osmr
LEFT JOIN osm_pt.osm_route_master_route rmr ON osmr.osm_route_id = rmr.osm_route_id
LEFT JOIN osm_pt.osm_route_master orm ON orm.osm_route_master_id = rmr.osm_route_master_id
WHERE osmr.network != orm.network OR osmr.operator != orm.operator
""")
    List<OsmRouteNetworkIssue> getNetworkIssues();
}

