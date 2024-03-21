package nl.haltedata.gtfs.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GtfsSpecialQuayRepository extends JpaRepository<GtfsSpecialQuay, Long>{
    
    List<GtfsSpecialQuay> findAllByOperator(String operator);

}
