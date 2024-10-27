package nl.haltedata.osm.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmPtLine;
import nl.haltedata.osm.dto.OsmPtLineRepository;

@RestController
@RequestMapping("/osm/line")
public class OsmPtLineController {
    
    @Inject
    private OsmPtLineRepository lineRepository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/list")
    public List<OsmPtLine> getNetworks(@RequestParam String network) throws Exception {
        return lineRepository.findByNetwork(network);
    }
}