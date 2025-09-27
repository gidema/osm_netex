package nl.haltedata.analysis.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.RouteMatch;
import nl.haltedata.analysis.dto.RouteMatchRepository;

@RestController
public class RouteMatchController {
    
    @Inject
    private RouteMatchRepository repository;

    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/route_match/{id}")
    public Optional<RouteMatch> findById(@PathVariable("id") Long id) throws Exception {
        var result = repository.findById(id);
        return result;
    }

    /**
     * End point to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/network/{administrativeZone}/route_match")
    public List<RouteMatch> getByAdministrativeZone(@PathVariable("administrativeZone") String administrativeZone) throws Exception {
        return repository.findByAdministrativeZone(administrativeZone);
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
//    @SuppressWarnings("exports")
//    @CrossOrigin(origins = "http://localhost:4200")
//    @GetMapping("/route_match")
//    public List<RouteMatch> getByLineId(@RequestParam("lineId") Long lineId) throws Exception {
//            return repository.findByLineId(lineId);
//    }

}