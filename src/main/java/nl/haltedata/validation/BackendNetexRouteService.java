package nl.haltedata.validation;

import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Inject;
import nl.haltedata.core.services.NetexRouteService;
import nl.haltedata.netex.dto.DimNetexRoute;
import nl.haltedata.netex.dto.DimNetexRouteRepository;

public class BackendNetexRouteService implements NetexRouteService {
    private final Map<String, DimNetexRoute> routeCache = new HashMap<>();
    
    @Inject
    DimNetexRouteRepository routeRepository;
    
    private BackendNetexRouteService() {
        // Empty private constructor for singleton class
    }
    
    public DimNetexRoute getRoute(String routeId) {
        var route = routeCache.get(routeId);
        if (route == null) {
            route = routeRepository.findById(routeId).get();
        }
        return route;
    }
}
