package nl.haltedata.analysis.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface LineMatchRepository extends CrudRepository<LineMatch, Integer> {
    List<LineMatch> findByNetwork(String network);
}
