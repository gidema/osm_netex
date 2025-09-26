package nl.haltedata.osm.dto;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OsmPtLineRepository extends CrudRepository<OsmPtLine, Long> {

    @Query(nativeQuery = true, value = """
SELECT *
FROM osm_pt.osm_line
WHERE id IN (
    SELECT route_master_id
    FROM osm_pt.st_osm_network_line
    WHERE network_id = ?1)
ORDER BY line_sort;
""")
    List<OsmPtLine> findByOsmNetworkIdOrderByLineSort(Long networkId);
}

