package nl.haltedata.netex.dto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NetexLineRepository extends CrudRepository<NetexLine, String> {

    @Override
    @Query(value = "DELETE FROM NetexLine")
    default void deleteAll() {
        // TODO Auto-generated method stub
        
    }
    //
    
}

