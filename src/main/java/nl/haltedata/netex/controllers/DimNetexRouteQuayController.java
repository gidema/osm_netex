package nl.haltedata.netex.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.netex.dto.DimNetexRouteQuay;
import nl.haltedata.netex.dto.DimNetexRouteQuayRepository;

@RestController
public class DimNetexRouteQuayController {

    @Inject
    private DimNetexRouteQuayRepository repository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/netex/route/{id}/quays")
    public List<DimNetexRouteQuay> byRouteId(@PathVariable("id") String id) throws Exception {
        return repository.findByRouteId(id);
    }
}
