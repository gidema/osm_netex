package nl.haltedata.osm.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmNetworkDto;

@RestController
@RequestMapping("/osm/network")
public class OsmNetworkController {
    
    @Inject
    private OsmNetworkService networkService;

    /**
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public Optional<OsmNetworkDto> getById(@PathVariable Long id) throws Exception {
        return networkService.findById(id);
    }
}