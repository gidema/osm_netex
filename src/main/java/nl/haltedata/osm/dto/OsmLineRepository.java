package nl.haltedata.osm.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;

public interface OsmLineRepository extends CrudRepository<OsmLine, Long> {

    @Query(nativeQuery = true, value = """
SELECT *
FROM osm_pt.osm_line
WHERE id IN (
    SELECT route_master_id
    FROM osm_pt.st_osm_network_line
    WHERE network_id = ?1)
ORDER BY line_sort;
""")
    List<OsmLine> findByOsmNetworkIdOrderByLineSort(Long networkId);
    
    @Override
    @EntityGraph(value="osmLineWithNetworksAndRoutes", type=EntityGraphType.FETCH)
    Optional<OsmLine> findById(Long id);
}

