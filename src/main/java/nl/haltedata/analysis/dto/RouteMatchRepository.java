package nl.haltedata.analysis.dto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RouteMatchRepository extends CrudRepository<RouteMatch, Long> {
//    @Override
//    @Query("""
//      SELECT rm
//      FROM RouteMatch rm
//      LEFT JOIN FETCH rm.issues
//      WHERE rm.id = :id
//""")
    @Override
//    @EntityGraph(value = "routeMatch-with-issues")
//    @Query("""
//      SELECT rm
//      FROM RouteMatch rm
//      WHERE rm.id = :id
//""")
    Optional<RouteMatch> findById(Long id);

    @EntityGraph(value = "routeMatch-with-issues")
    List<RouteMatch> findByLineMatch(LineMatch lineMatch);

//    @EntityGraph(value = "routeMatch-with-issues")
    @Query("""
SELECT rm
FROM RouteMatch rm
WHERE rm.lineMatch.id = :id
""")
    List<RouteMatch> findByLineMatchId(Long id);

//    List<RouteMatch> findByOsmRouteId(Long osmRouteId);

    @Query(value = """
SELECT DISTINCT RouteMatch
FROM RouteMatch rm
JOIN FETCH LineMatch lm ON lm = rm.lineMatch
WHERE lm IN(:lineMatches)
""")
    List<RouteMatch> findByLineMatches(@Param("lineMatches") List<LineMatch> lineMatches);

    List<RouteMatch> findByAdministrativeZone(String administrativeZone);
    
    @Override
//    @EntityGraph(value = "routeMatch-with-issues")
    Set<RouteMatch> findAll();

}
