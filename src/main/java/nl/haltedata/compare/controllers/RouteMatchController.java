package nl.haltedata.compare.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.compare.dto.RouteMatch;
import nl.haltedata.compare.dto.RouteMatchRepository;

@RestController
public class RouteMatchController {
    
    @Inject
    private RouteMatchRepository repository;

    /**
     * End point to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/network/{network}/route_match")
    public List<RouteMatch> getByLineId(@PathVariable("network") String network) throws Exception {
            return repository.findByNetwork(network);
    }

    /**
     * End point to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/route_match/osm/{osmRouteId}")
    public List<RouteMatch> getByOsmRouteId(@PathVariable("osmRouteId") Long osmRouteId) throws Exception {
            return repository.findByOsmRouteId(osmRouteId);
    }

    /**
     * End point to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/route_match")
    public List<RouteMatch> getByLineId(@RequestParam("lineId") Long lineId) throws Exception {
            return repository.findByLineId(lineId);
    }

    /**
     * End point to list the data.
     * @return 
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/route/{id}")
    public ResponseEntity<RouteMatch> getById(@PathVariable("id") Long routeId) throws Exception {
        return repository.findById(routeId)
                .map(route -> new ResponseEntity<>(route, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}