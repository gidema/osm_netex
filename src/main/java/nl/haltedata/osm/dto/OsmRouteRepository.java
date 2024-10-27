package nl.haltedata.osm.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface OsmRouteRepository extends CrudRepository<OsmRoute, Long> {

    List<OsmRoute> findByOsmLineId(Long osmLineId);
}

