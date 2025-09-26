package nl.haltedata.analysis.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.LineMatch;
import nl.haltedata.analysis.dto.LineMatchRepository;

@RestController
public class LineMatchController {
    
    @Inject
    private LineMatchRepository repository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/line")
    public List<LineMatch> getByQuery(@RequestParam(name ="network", required = false) String network,
        @RequestParam(name = "administrativeZone", required = false) String administrativeZone) throws Exception {
        if (administrativeZone != null) {
            return repository.findByAdministrativeZoneOrderByLineSort(administrativeZone);
        }
        return repository.findByNetwork(network);
    }
    
    /**
     * Endpoint to list the data.
     * @return 
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/line/{id}")
    public ResponseEntity<LineMatch> getById(@PathVariable("id") Integer lineId) throws Exception {
        return repository.findById(lineId)
                .map(route -> new ResponseEntity<>(route, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }


}