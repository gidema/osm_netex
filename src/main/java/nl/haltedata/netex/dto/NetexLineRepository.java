package nl.haltedata.netex.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
public interface NetexLineRepository extends CrudRepository<NetexLine, String> {

    List<NetexLine> findByNetwork(String network);
}

