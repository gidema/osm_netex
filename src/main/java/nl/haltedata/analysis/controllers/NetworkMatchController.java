package nl.haltedata.analysis.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.NetworkMatch;
import nl.haltedata.analysis.dto.NetworkMatchRepository;

@RestController
@RequestMapping("/network")
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
    @GetMapping()
    public Iterable<NetworkMatch> findAll() throws Exception {
        return repository.findAllByOrderByName();
    }

    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public Optional<NetworkMatch> findById(@PathVariable("id") String id) throws Exception {
        return repository.findById(id);
    }
}