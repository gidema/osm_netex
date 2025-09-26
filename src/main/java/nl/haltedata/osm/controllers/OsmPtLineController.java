package nl.haltedata.osm.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmPtLine;
import nl.haltedata.osm.dto.OsmPtLineRepository;

@RestController
public class OsmPtLineController {
    
    @Inject
    private OsmPtLineRepository lineRepository;

    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/osm/line/{id}")
    public Optional<OsmPtLine> getById(@PathVariable("id") Long id) throws Exception {
        return lineRepository.findById(id);
    }
    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/osm/line")
    public List<OsmPtLine> findByNetwork(@RequestParam("networkId") Long id) throws Exception {
        return lineRepository.findByOsmNetworkIdOrderByLineSort(id);
    }
}