package nl.haltedata.netex.dto;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NetexRouteDataRepository extends CrudRepository<NetexRouteData, String> {

    /**
     * Find all NeTeX routes on a line between two stop places with a number of quays
     * between minQuayCount and maxQuayCount.
     * 
     * @param lineNumber
     * @param startStopPlaceCode
     * @param endStopPlaceCode
     * @param minQuayCount
     * @param maxQuayCount
     * @return A list of 
     */
    @Query("""
from NetexRouteData as n 
where n.lineNumber = ?1 AND n.startStopPlaceCode = ?2 AND n.endStopPlaceCode = ?3
    AND n.quayCount BETWEEN ?4 AND ?5""")
    List<NetexRouteData> findByEndpoints(String lineNumber, String startStopPlaceCode, String endStopPlaceCode,
            Integer minQuayCount, Integer maxQuayCount);
    //
    
}

