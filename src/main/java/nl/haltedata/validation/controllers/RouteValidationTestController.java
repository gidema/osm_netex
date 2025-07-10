package nl.haltedata.validation.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.netex.dto.NetexRouteVariantQuayRepository;
import nl.haltedata.netex.dto.NetexRouteVariantRepository;
import nl.haltedata.osm.dto.OsmRouteQuayRepository;
import nl.haltedata.osm.dto.OsmRouteRepository;
import nl.haltedata.validation.RouteValidator;

@RestController
public class RouteValidationTestController {
    
    @Inject
    private OsmRouteRepository osmRouteRepository;

    @Inject
    private NetexRouteVariantRepository netexRouteVariantRepository;
    
    @Inject
    private OsmRouteQuayRepository osmRouteQuayRepository;

    @Inject
    private NetexRouteVariantQuayRepository netexRouteVariantQuayRepository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/route/validate/test")
    public String test() throws Exception {
        var osmRoute = osmRouteRepository.findById(906385L).get();
        var netexRouteVariant = netexRouteVariantRepository.findById(255428L).get();
        var osmQuays = osmRouteQuayRepository.findByOsmRouteId(906385L);
        var netexQuays = netexRouteVariantQuayRepository.findByVariantId(255428L);
        var routeValidator = new RouteValidator(osmRoute, netexRouteVariant, osmQuays, netexQuays);
        var results = routeValidator.run();
        return results.getIssueReport();
    }
}