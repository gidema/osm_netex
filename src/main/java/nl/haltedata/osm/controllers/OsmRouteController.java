package nl.haltedata.osm.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmRouteDto;

@RestController
public class OsmRouteController {
    
    @Inject
    private OsmRouteService routeService;

    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/osm/route/{id}")
    public ResponseEntity<OsmRouteDto> findById(@PathVariable("id") Long id) throws Exception {
        return routeService.findById(id)
                .map(route -> new ResponseEntity<>(route, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

}