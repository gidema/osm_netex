package nl.haltedata.analysis.dto;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

public interface NetworkMatchRepository extends CrudRepository<NetworkMatch, String> {

    @EntityGraph(value = "networkMatch-list", type=EntityGraphType.FETCH)
    public List<NetworkMatch> findAllByOrderByName();
}
