package nl.haltedata.netex.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.netex.dto.NetexRouteQuayRepository;

@RestController
public class NetexRouteQuayController {

    @Inject
    private NetexRouteQuayRepository repository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/netex/routequays/{id}")
    public List<?> getRoute(@PathVariable("id") String id) throws Exception {
        return repository.queryByRouteIdOrderByQuayIndex(id);
    }
}
