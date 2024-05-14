package nl.haltedata.netex.mapping;

import org.rutebanken.netex.model.PointOnRoute;
import org.rutebanken.netex.model.Route;

import nl.haltedata.netex.dto.NetexPointOnRoute;
import nl.haltedata.tools.ParentChildMapper;

public class PointOnRouteMapper implements ParentChildMapper<PointOnRoute, NetexPointOnRoute, Route> {
    
    @Override
    public NetexPointOnRoute map(PointOnRoute routePoint, Route route) {
        var rp = new NetexPointOnRoute();

        rp.setPointOnRouteId(routePoint.getId());
        rp.setRouteId(route.getId());
        rp.setSequence(routePoint.getOrder().intValue());
        rp.setRoutePointRef(routePoint.getPointRef().getValue().getRef());
        return rp;
    }
}
