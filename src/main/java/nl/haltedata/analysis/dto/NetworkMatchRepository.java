package nl.haltedata.analysis.dto;

import org.springframework.data.repository.CrudRepository;

public interface NetworkMatchRepository extends CrudRepository<NetworkMatch, String> {

    public Iterable<NetworkMatch> findAllByOrderByName();
}
