package nl.haltedata.osm.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface OsmRouteQuayRepository extends CrudRepository<OsmRouteQuay, OsmRouteQuayId> {

    List<OsmRouteQuay> findByOsmRouteId(Long osmRouteId);
}

