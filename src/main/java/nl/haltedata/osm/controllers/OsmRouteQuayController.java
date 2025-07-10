package nl.haltedata.osm.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmRouteQuayRepository;

@RestController
public class OsmRouteQuayController {
    
    @Inject
    private OsmRouteQuayRepository routeQuayRepository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/osm/route/{osmRouteId}/quays")
    public List<?> getByRouteId(@PathVariable("osmRouteId") Long osmRouteId) throws Exception {
        return routeQuayRepository.findByOsmRouteId(osmRouteId);
    }
}