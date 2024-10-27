package nl.haltedata.chb.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.chb.dto.DimChbQuay;
import nl.haltedata.chb.dto.DimChbQuayRepository;

@RestController
public class DimChbQuayController {
    
    @Inject
    private DimChbQuayRepository repository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/chb/quay/{quayCodes}")
    public Iterable<DimChbQuay> getRouteMatches(@PathVariable("quayCodes") List<String> quayCodes) throws Exception {
        return repository.findAllByQuayCodeIn(quayCodes);
    }
}