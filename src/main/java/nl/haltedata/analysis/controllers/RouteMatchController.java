package nl.haltedata.analysis.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.RouteMatchDto;
import nl.haltedata.analysis.services.RouteMatchService;

@RestController
public class RouteMatchController {
    
    @Inject
    private RouteMatchService service;

    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/route-match/{id}")
    public Optional<RouteMatchDto> findById(@PathVariable("id") Long id) throws Exception {
        var result = service.findById(id);
        return result;
    }

    /**
     * End point to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
//    @SuppressWarnings("exports")
//    @CrossOrigin(origins = "http://localhost:4200")
//    @GetMapping("/network/{administrativeZone}/route_match")
//    public List<RouteMatchDto> findByAdministrativeZone(@PathVariable("administrativeZone") String administrativeZone) throws Exception {
//        return service.findByAdministrativeZone(administrativeZone);
//    }

    /**
     * End point to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/network/{administrativeZone}/route_match")
    public List<RouteMatchDto> findByLineMatchId(@PathVariable("id") Long id) throws Exception {
        return service.findByLineMatchId(id);
    }

}