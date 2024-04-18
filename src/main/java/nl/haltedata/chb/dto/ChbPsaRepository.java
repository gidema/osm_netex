package nl.haltedata.chb.dto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ChbPsaRepository extends CrudRepository<ChbPsa, String> {
    
    @Override
    @Query(value = "DELETE FROM ChbPsa")
    default void deleteAll() {
        // TODO Auto-generated method stub
        
    }

}

