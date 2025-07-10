package nl.haltedata.validation.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.validation.model.RouteValidationRepository;

@RestController
public class RouteValidationController {
    
    @Inject
    private RouteValidationRepository repository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/osm/route/validate/{network}")
    public List<?> getByNetwork(@PathVariable("network") String network) throws Exception {
        return repository.findByNetworkOrderByLine(network);
    }
}