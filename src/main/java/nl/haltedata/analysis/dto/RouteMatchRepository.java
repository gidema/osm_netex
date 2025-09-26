package nl.haltedata.analysis.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RouteMatchRepository extends CrudRepository<RouteMatch, Long> {
    @Override
    @Query("""
      SELECT rm
      FROM RouteMatch rm
      LEFT JOIN FETCH rm.issues
      WHERE rm.id = :id
""")
    Optional<RouteMatch> findById(Long id);

    List<RouteMatch> findByLineId(Long lineId);

    List<RouteMatch> findByOsmRouteId(Long osmRouteId);

    List<RouteMatch> findByAdministrativeZone(String administrativeZone);
}
