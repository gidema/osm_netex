package nl.haltedata.netex.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.netex.dto.NetexRouteVariantQuayRepository;

@RestController
public class NetexRouteVariantQuayController {

    @Inject
    private NetexRouteVariantQuayRepository repository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/netex/route-variant/{id}/quays")
    public List<?> byVariantId(@PathVariable("id") Long id) throws Exception {
        return repository.findByVariantId(id);
    }
}
