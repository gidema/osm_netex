package nl.haltedata.osm.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmRouteQuayDto;

@RestController
public class OsmRouteQuayController {
    
    @Inject
    private OsmRouteQuayService routeQuayService;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/osm/route-quay/{osmRouteQuayId}")
    public Optional<OsmRouteQuayDto> findById(@PathVariable("osmRouteQuayId") Long routeQuayId) throws Exception {
        return routeQuayService.findById(routeQuayId);
    }
}