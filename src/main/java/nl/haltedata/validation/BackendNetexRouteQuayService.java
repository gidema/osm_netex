package nl.haltedata.validation;

import java.util.List;

import jakarta.inject.Inject;
import nl.haltedata.core.services.NetexRouteQuayService;
import nl.haltedata.netex.dto.NetexRouteQuay;
import nl.haltedata.netex.dto.NetexRouteQuayRepository;

public class BackendNetexRouteQuayService implements NetexRouteQuayService {
    @Inject
    NetexRouteQuayRepository routeQuayRepository;
    
    @Override
    public List<NetexRouteQuay> getByRouteId(String routeId) {
        return routeQuayRepository.queryByRouteIdOrderByQuayIndex(routeId);
    }
}
