package nl.haltedata.osm.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface OsmRoutePlatformRepository extends CrudRepository<DimOsmRoutePlatform, OsmRouteQuayId> {

    List<DimOsmRoutePlatform> findByOsmRouteId(Long osmRouteId);
}

