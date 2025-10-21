package nl.haltedata.netex.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.netex.dto.NetexLineDto;

@RestController
public class NetexLineController {
    
    @Inject
    private NetexLineService service;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/netex/line/{id}")
    public ResponseEntity<NetexLineDto> findById(@PathVariable("id") String id) throws Exception {
        return service.findById(id)
                .map(route -> new ResponseEntity<>(route, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
//    
//    @SuppressWarnings("exports")
//    @CrossOrigin(origins = "http://localhost:4200")
//    @GetMapping("/netex/line")
//    public List<NetexLineDto> findByAdministrativeZone(@RequestParam("administrativeZone") String administrativeZone) throws Exception {
//        return service.findByAdministrativeZone(administrativeZone);
//    }
}