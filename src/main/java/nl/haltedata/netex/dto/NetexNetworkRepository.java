package nl.haltedata.netex.dto;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface NetexNetworkRepository extends CrudRepository<NetexNetwork, String> {

    public Iterable<NetexNetwork> findAllByOrderByName();

    public Optional<NetexNetwork> findByAdministrativeZone(String administrativeZone);
    
}

