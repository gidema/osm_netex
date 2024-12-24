package nl.haltedata.netex.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
public interface NetexQuaySequenceRepository extends CrudRepository<NetexQuaySequence, Long> {

    @Override
    Optional<NetexQuaySequence> findById(Long sequenceId);
    
//    List<NetexQuaySequence> findByLineId(String lineId);

}

