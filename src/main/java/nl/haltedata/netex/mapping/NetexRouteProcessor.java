package nl.haltedata.netex.mapping;

import org.rutebanken.netex.model.Route;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import nl.haltedata.netex.dto.NetexRoute;

@Component
public class NetexRouteProcessor implements ItemProcessor<Route, NetexRoute> {
 
    @Override
    public NetexRoute process(Route route) throws Exception {
        var netexRoute = new NetexRoute();
        netexRoute.setId(route.getId());
        netexRoute.setName(route.getName() != null ? route.getName().getValue() : null);
        netexRoute.setLineRef(route.getLineRef().getValue().getRef());
        netexRoute.setDirectionType(route.getDirectionType().toString());
        return netexRoute;       
    }
}