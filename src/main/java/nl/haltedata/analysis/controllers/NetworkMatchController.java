package nl.haltedata.analysis.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.NetworkMatch;
import nl.haltedata.analysis.dto.NetworkMatchRepository;

@RestController
public class NetworkMatchController {
    
    @Inject
    private NetworkMatchRepository repository;

    /**
     * Endpoint to list the data.
     * @return 
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/network/list")
    public Iterable<NetworkMatch> findAll() throws Exception {
        return repository.findAll();
    }


}