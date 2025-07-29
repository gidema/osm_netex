package nl.haltedata.osm.dto;

import org.springframework.data.repository.CrudRepository;

public interface OsmPtNetworkRepository extends CrudRepository<OsmPtNetwork, String> {

    public Iterable<OsmPtNetwork> findAllByCountryCode(String country);
}

