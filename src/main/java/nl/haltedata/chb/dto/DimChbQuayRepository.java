package nl.haltedata.chb.dto;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

public interface DimChbQuayRepository extends CrudRepository<DimChbQuay, String> {
    
    public Iterable<DimChbQuay> findAllByQuayCodeIn(Collection<String> quayCodes);
}

