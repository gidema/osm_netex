package nl.haltedata.netex.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.netex.dto.NetexQuayInSequence;
import nl.haltedata.netex.dto.NetexQuayInSequenceRepository;

@RestController
public class NetexQuayInSequenceController {

    @Inject
    private NetexQuayInSequenceRepository repository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/netex/quay-sequence/{id}/quays")
    public List<NetexQuayInSequence> bySequenceId(@PathVariable("id") Long id) throws Exception {
        return repository.findBySequenceId(id);
    }
}
