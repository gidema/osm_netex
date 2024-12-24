package nl.haltedata.netex.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
public interface NetexQuayInSequenceRepository extends CrudRepository<NetexQuayInSequence, Long> {

    List<NetexQuayInSequence> findBySequenceId(Long sequenceId);
}

