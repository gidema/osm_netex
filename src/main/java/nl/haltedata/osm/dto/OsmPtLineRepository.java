package nl.haltedata.osm.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface OsmPtLineRepository extends CrudRepository<OsmPtLine, Long> {

    List<OsmPtLine> findByNetwork(String network);
}

