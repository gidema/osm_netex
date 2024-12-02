package nl.haltedata.compare.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.compare.dto.RouteMatch;
import nl.haltedata.compare.dto.RouteMatchRepository;

@RestController
public class RouteMatchController {
    
    @Inject
    private RouteMatchRepository repository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/routematches/{osmRouteId}")
    public List<RouteMatch> getRouteMatches(@PathVariable("osmRouteId") Long osmRouteId) throws Exception {
        return repository.findByOsmRouteId(osmRouteId);
    }
}