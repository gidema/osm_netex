package nl.haltedata.analysis.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.RouteMatch;
import nl.haltedata.analysis.dto.RouteMatchRepository;

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
//    public List<RouteMatch> getByNetwork(@PathVariable("network") String network) throws Exception {
//            return repository.findByNetwork(network);
//    }
    public List<RouteMatch> getByNetwork(@PathVariable("network") String network) throws Exception {
        return repository.findByNetwork(network);
//                .map(<RouteMatch>route -> new ResponseEntity<>(route, HttpStatus.OK))
//                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
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

}