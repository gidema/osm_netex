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
import nl.haltedata.netex.dto.NetexRoute;
import nl.haltedata.netex.dto.NetexRouteRepository;

@RestController
public class NetexRouteController {
    
    @Inject
    private NetexRouteRepository repository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/netex/route/{id}")
    public ResponseEntity<NetexRoute> getRoute(@PathVariable("id") String id) throws Exception {
        return repository.findById(id)
                .map(route -> new ResponseEntity<>(route, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/netex/route")
    public List<NetexRoute> getRouteByLine(@RequestParam("lineId") String lineRef) throws Exception {
        return repository.findByLineRef(lineRef);
    }
}