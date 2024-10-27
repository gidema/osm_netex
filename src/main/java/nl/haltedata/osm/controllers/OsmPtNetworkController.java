package nl.haltedata.osm.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmPtNetwork;
import nl.haltedata.osm.dto.OsmPtNetworkRepository;

@RestController
@RequestMapping("/osm/network")
public class OsmPtNetworkController {
    
    @Inject
    private OsmPtNetworkRepository networkRepository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/list")
    public List<OsmPtNetwork> getNetworks() throws Exception {
            return (List<OsmPtNetwork>) networkRepository.findAll();
    }
}