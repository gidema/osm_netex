package nl.haltedata.chb.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ChbQuayRepository extends CrudRepository<ChbQuay, String> {
    
//    @Override
//    @Query(value = "DELETE FROM ChbQuay")
//    default void deleteAll() {
//        // TODO Auto-generated method stub
//        
//    }

    public Iterable<ChbQuay> findAllByQuayCodeIn(List<String> quayCodes);
}

