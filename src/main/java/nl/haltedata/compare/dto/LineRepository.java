package nl.haltedata.compare.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface LineRepository extends CrudRepository<Line, Integer> {
    List<Line> findByNetwork(String network);
}
