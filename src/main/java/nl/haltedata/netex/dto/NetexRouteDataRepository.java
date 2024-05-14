package nl.haltedata.netex.dto;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NetexRouteDataRepository extends CrudRepository<NetexRouteData, String> {

    /**
     * Find all NeTeX routes on a line between two stopplaces with a number of quays
     * between minQuayCount and maxQuayCount.
     * 
     * @param lineNumber
     * @param startStopplaceCode
     * @param endStopplaceCode
     * @param minQuayCount
     * @param maxQuayCount
     * @return A list of 
     */
    @Query("""
from NetexRouteData as n 
where n.lineNumber = ?1 AND n.startStopplaceCode = ?2 AND n.endStopplaceCode = ?3
    AND n.quayCount BETWEEN ?4 AND ?5""")
    List<NetexRouteData> findByEndpoints(String lineNumber, String startStopplaceCode, String endStopplaceCode,
            Integer minQuayCount, Integer maxQuayCount);
    //
    
}

