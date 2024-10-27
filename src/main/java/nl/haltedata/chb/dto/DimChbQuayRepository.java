package nl.haltedata.chb.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface DimChbQuayRepository extends CrudRepository<DimChbQuay, String> {
    
    public Iterable<DimChbQuay> findAllByQuayCodeIn(List<String> quayCodes);
}

