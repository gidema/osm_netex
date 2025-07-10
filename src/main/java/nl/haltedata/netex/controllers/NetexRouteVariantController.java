package nl.haltedata.netex.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.netex.dto.NetexRouteVariant;
import nl.haltedata.netex.dto.NetexRouteVariantRepository;

@RestController
public class NetexRouteVariantController {
    
    @Inject
    private NetexRouteVariantRepository repository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/netex/route-variant/{id}")
    public ResponseEntity<NetexRouteVariant> getVariant(@PathVariable("id") Long id) throws Exception {
        return repository.findById(id)
                .map(route -> new ResponseEntity<>(route, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
    
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/netex/route-variant")
    public List<NetexRouteVariant> getByLine(@RequestParam("lineId") String lineRef) throws Exception {
        return repository.findByLineRef(lineRef);
    }

}