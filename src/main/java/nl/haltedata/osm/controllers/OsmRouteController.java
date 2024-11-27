package nl.haltedata.osm.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmRoute;
import nl.haltedata.osm.dto.OsmRouteRepository;

@RestController
public class OsmRouteController {
    
    @Inject
    private OsmRouteRepository routeRepository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/osm/route")
    public List<OsmRoute> getByLine(@RequestParam("lineId") Long lineId) throws Exception {
        return routeRepository.findByOsmLineId(lineId);
    }
    
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/osm/route/{id}")
    public ResponseEntity<OsmRoute> getRoute(@PathVariable("id") Long id) throws Exception {
        return routeRepository.findById(id)
                .map(route -> new ResponseEntity<>(route, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

}