package nl.haltedata.analysis.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LineMatchRepository extends CrudRepository<LineMatch, Long> {
    @Override
    @Query("""
      SELECT lm
      FROM LineMatch lm
      LEFT JOIN FETCH lm.routes AS route
      WHERE lm.id = :id
      ORDER BY route.directionType
""")
    Optional<LineMatch> findById(Long id);
    List<LineMatch> findByNetwork(String network);
    List<LineMatch> findByAdministrativeZoneOrderByLineSort(String administrativeZone);
}
