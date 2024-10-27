package nl.haltedata.check.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import nl.haltedata.check.model.DimNetexRoute;
import nl.haltedata.check.model.NetexRouteQuay;
import nl.haltedata.check.model.RouteMatch;

public class NetexRestClient {
    private final RestClient restClient = RestClient.create();
    private final ChbQuayService quayService = ChbQuayService.getInstance();
    private final NetexRouteService routeService = NetexRouteService.getInstance();

    public DimNetexRoute getRoute(String routeId) {
        return routeService.getRoute(routeId);
    }
            
    public List<NetexRouteQuay> getRouteQuays(String routeId) {
        List<NetexRouteQuay> routeQuays = restClient.get()
            .uri("http://localhost:8080/netex/routequays?netexRouteId={routeId}", routeId)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {});
        Set<String> quayCodes = routeQuays.stream().map(rq -> rq.getQuayCode()).collect(Collectors.toSet());
        quayService.fetchMissingCodes(quayCodes);
        routeQuays.forEach(routeQuay -> {
            var dimChbQuay = quayService.getQuay(routeQuay.getQuayCode());
//            routeQuay.setQuayName(dimChbQuay.getQuayName());
        });
        return routeQuays;
    }

    public List<RouteMatch> getRouteMatches(Long osmRouteId) {
        return restClient.get().uri("http://localhost:8080/routematches/" + osmRouteId).retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
