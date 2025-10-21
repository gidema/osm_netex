package nl.haltedata.analysis.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.NetworkMatchDto;
import nl.haltedata.analysis.services.NetworkMatchService;

@RestController
@RequestMapping("/network")
public class NetworkMatchController {
    
    @Inject
    private NetworkMatchService service;

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
    public Iterable<NetworkMatchDto> findAll() throws Exception {
        return service.findAllByOrderByName();
    }

    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public Optional<NetworkMatchDto> findById(@PathVariable("id") String id) throws Exception {
        return service.findById(id);
    }
}