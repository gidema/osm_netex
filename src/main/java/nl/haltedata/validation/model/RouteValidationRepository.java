package nl.haltedata.validation.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RouteValidationRepository extends CrudRepository<RouteValidationStatus, Long> {

    List<RouteValidationStatus> findByNetworkOrderByLine(String network);
}

