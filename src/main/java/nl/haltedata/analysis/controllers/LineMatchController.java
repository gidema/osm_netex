package nl.haltedata.analysis.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.LineMatchDto;
import nl.haltedata.analysis.services.LineMatchService;

@RestController
public class LineMatchController {
    
    @Inject
    private LineMatchService lineMatchService;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/line-match")
    @Transactional(readOnly = true)
    public List<LineMatchDto> findByQuery(@RequestParam(name = "administrativeZone") String administrativeZone) throws Exception {
        return lineMatchService.findByAdministrativeZone(administrativeZone);
    }
    
    /**
     * Endpoint to list the data.
     * @return 
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/line-match/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<LineMatchDto> findById(@PathVariable("id") Long lineId) throws Exception {
        var response = lineMatchService.findById(lineId)
                .map(route -> new ResponseEntity<>(route, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        return response;
    }


}