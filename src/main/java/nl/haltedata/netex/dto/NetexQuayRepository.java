package nl.haltedata.netex.dto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NetexQuayRepository extends CrudRepository<NetexQuay, String> {

    @Override
    @Query(value = "DELETE FROM NetexQuay")
    default void deleteAll() {
        // TODO Auto-generated method stub
        
    }
    //
    
}

