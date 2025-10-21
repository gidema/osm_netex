package nl.haltedata.analysis.dto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
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
      FROM LineMatch lm
      JOIN NetworkMatch nm ON lm.networkMatch = nm
      WHERE nm.administrativeZone = :adminZone
""")
    List<LineMatch> findByAdministrativeZoneOrderByLineSort(@Param("adminZone")String adminZone);
    
    List<LineMatch> findByIdIn(Set<Long> ids);
}
