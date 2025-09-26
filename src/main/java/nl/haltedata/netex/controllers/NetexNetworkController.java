package nl.haltedata.netex.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.netex.dto.NetexNetwork;
import nl.haltedata.netex.dto.NetexNetworkRepository;

@RestController
@RequestMapping("/netex/network")
public class NetexNetworkController {
    
    @Inject
    private NetexNetworkRepository networkRepository;

    /**
     * Endpoint to list the data.
     *
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping()
    public List<NetexNetwork> getNetworks() throws Exception {
            return (List<NetexNetwork>) networkRepository.findAllByOrderByName();
    }
    
    /**
     * Endpoint for a single NeTex network
    *
    * @return
    * @throws Exception if any error occurs during job launch.
    */
   @SuppressWarnings("exports")
   @CrossOrigin(origins = "http://localhost:4200")
   @GetMapping("/{administrativeZone}")
   public Optional<NetexNetwork> getById(@PathVariable String administrativeZone) throws Exception {
           return networkRepository.findByAdministrativeZone(administrativeZone);
   }

}