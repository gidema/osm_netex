package nl.haltedata.netex.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
public interface DimNetexRouteQuayRepository extends CrudRepository<DimNetexRouteQuay, String> {

    List<DimNetexRouteQuay> findByRouteId(String routeId);
}

