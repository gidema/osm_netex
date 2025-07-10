package nl.haltedata.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import nl.haltedata.core.services.OsmRouteService;
import nl.haltedata.osm.dto.DimOsmRouteQuay;
import nl.haltedata.osm.dto.OsmRoute;
import nl.haltedata.osm.dto.DimOsmRouteQuayRepository;
import nl.haltedata.osm.dto.OsmRouteRepository;

public class BackendOsmRouteService implements OsmRouteService {
    private final Map<Long, OsmRoute> routeCache = new HashMap<>();

    private final String host;
    
    @Inject
    OsmRouteRepository routeRepository;
    @Inject
    DimOsmRouteQuayRepository routeQuayRepository;
    
    private BackendOsmRouteService(String host) {
        this.host = host;
    }
    
    @Override
    public OsmRoute getRoute(Long routeId) {
        OsmRoute route = routeCache.get(routeId);
        if (route == null) {
            route = routeRepository.findById(routeId).get();
            if (route != null) {
                routeCache.put(route.getOsmRouteId(), route);
            }
        }
        return route;
    }
    
    public List<DimOsmRouteQuay> getRouteQuays(Long routeId) {
        return routeQuayRepository.findByOsmRouteId(routeId);
    }
}
