package nl.haltedata.netex.controllers;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.netex.dto.NetexRouteVariantDto;
import nl.haltedata.netex.dto.NetexRouteVariantRepository;

@RestController
public class NetexRouteVariantService {
    
    @Inject
    private NetexRouteVariantRepository repository;

    @Inject
    private ModelMapper modelMapper;

    /**
     * @return
     * @throws Exception if any error occurs during job launch.
     */
    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public Optional<NetexRouteVariantDto> findById(Long id) throws Exception {
        return repository.findById(id).map(variant -> {
            return modelMapper.map(variant, NetexRouteVariantDto.class);
        });
    }
    
//    @SuppressWarnings("exports")
//    @CrossOrigin(origins = "http://localhost:4200")
//    @GetMapping("/netex/route-variant")
//    public List<NetexRouteVariant> findByLineRef(@RequestParam("lineRef") String lineRef) throws Exception {
//        return repository.findByLineRef(lineRef);
//    }

}