package nl.haltedata.netex.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.netex.dto.NetexNetworkDto;

@RestController
@RequestMapping("/netex/network")
public class NetexNetworkController {
    
    @Inject
    private NetexNetworkService networkService;

//    /**
//     * Endpoint to list the data.
//     *
//     * @return
//     * @throws Exception if any error occurs during job launch.
//     */
//    @SuppressWarnings("exports")
//    @CrossOrigin(origins = "http://localhost:4200")
//    @GetMapping()
//    public List<NetexNetwork> getNetworks() throws Exception {
//            return (List<NetexNetwork>) networkService.findAllByOrderByName();
//    }
    
    /**
     * Endpoint for a single NeTex network
    *
    * @return
    * @throws Exception if any error occurs during job launch.
    */
   @SuppressWarnings("exports")
   @CrossOrigin(origins = "http://localhost:4200")
   @GetMapping("/{administrativeZone}")
   public Optional<NetexNetworkDto> findById(@PathVariable String administrativeZone) throws Exception {
           return networkService.findByAdministrativeZone(administrativeZone);
   }

}