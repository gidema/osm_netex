package nl.haltedata.netex.dto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NetexPointOnRouteRepository extends CrudRepository<NetexPointOnRoute, String> {

    @Override
    @Query(value = "DELETE FROM NetexRoutePoint")
    default void deleteAll() {
        // TODO Auto-generated method stub
        
    }
    //
    
}

