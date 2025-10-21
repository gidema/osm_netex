package nl.haltedata.osm.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmLineDto;

@RestController
public class OsmLineController {
    
    @Inject
    private OsmLineService lineService;

    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/osm/line/{id}")
    public Optional<OsmLineDto> getById(@PathVariable("id") Long id) throws Exception {
        return lineService.findById(id);
    }
    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
//    @SuppressWarnings("exports")
//    @CrossOrigin(origins = "http://localhost:4200")
//    @GetMapping("/osm/line")
//    public List<OsmLineDto> findByNetwork(@RequestParam("networkId") Long id) throws Exception {
//        return lineService.findByOsmNetworkIdOrderByLineSort(id);
//    }
}