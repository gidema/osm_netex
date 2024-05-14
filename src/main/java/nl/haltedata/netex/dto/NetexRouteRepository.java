package nl.haltedata.netex.dto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
public interface NetexRouteRepository extends CrudRepository<NetexRoute, String> {

    @Override
    @Query(value = "DELETE FROM NetexRoute")
    default void deleteAll() {
        // TODO Auto-generated method stub
        
    }
    //
    
}

