package nl.haltedata.compare.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RouteRepository extends CrudRepository<Route, Long> {
    List<Route> findByLineId(Long lineId);
}
