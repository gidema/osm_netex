package nl.haltedata.osm.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

public interface OsmRouteRepository extends CrudRepository<OsmRoute, Long> {

    @Override
    @EntityGraph(value = "osmRouteWithQuays", type=EntityGraphType.FETCH)
    Iterable<OsmRoute> findAll();

    List<OsmRoute> findByOsmLineId(Long osmLineId);
    
    @Query("""
SELECT or
FROM OsmRoute or
JOIN RouteMatch rm ON rm.osmRoute = or
WHERE rm.lineMatch.id = :id
""")
    List<OsmRoute> findByLineMatchId(Long id);

    @Override
    @EntityGraph(value = "osmRouteWithQuays", type=EntityGraphType.FETCH)
    Optional<OsmRoute> findById(Long id);
    
    @Override
    @EntityGraph(value = "osmRouteWithQuays", type=EntityGraphType.FETCH)
    Iterable<OsmRoute> findAllById(Iterable<Long> ids);

    @Query("""
SELECT or
FROM OsmRoute or
JOIN RouteMatch rm ON rm.osmRoute = or
JOIN LineMatch lm ON rm.lineMatch = lm
WHERE lm.networkMatch.administrativeZone = :administrativeZone
""")
    List<OsmRoute> findByAdministrativeZone(String administrativeZone);

    @Modifying
    @Query("""
SELECT or
FROM OsmRoute or, OsmLine ol
WHERE ol IS NULL AND or.transportMode='bus'
""")
    void updateRouteNotInLineIssue();
}
