package nl.haltedata.compare.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface LineRepository extends CrudRepository<LineMatch, Integer> {
    List<LineMatch> findByNetwork(String network);
}
