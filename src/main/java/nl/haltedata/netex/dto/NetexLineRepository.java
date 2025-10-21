package nl.haltedata.netex.dto;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface NetexLineRepository extends CrudRepository<NetexLine, String> {

    @Override
    Optional<NetexLine> findById(String id);

//    @EntityGraph("netexLine-with-routeVariants")
//    List<NetexLine> findByAdministrativeZoneOrderByLineSort(String administrativeZone);
}

