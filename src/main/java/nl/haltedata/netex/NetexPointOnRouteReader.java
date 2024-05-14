package nl.haltedata.netex;

import java.util.Iterator;

import org.rutebanken.netex.model.PointOnRoute;
import org.rutebanken.netex.model.Route;
import org.springframework.batch.item.xml.StaxEventItemReader;

import nl.haltedata.netex.dto.NetexPointOnRoute;
import nl.haltedata.tools.ParentChildMapper;
import nl.haltedata.tools.StaxParentChildEventItemReader;

public class NetexPointOnRouteReader extends StaxParentChildEventItemReader<PointOnRoute, NetexPointOnRoute, Route> 
     {

    public NetexPointOnRouteReader(StaxEventItemReader<Route> parentReader,
            ParentChildMapper<PointOnRoute, NetexPointOnRoute, Route> mapper) {
        super(parentReader, mapper);
    }

    @Override
    protected Iterator<PointOnRoute> getChildIterator(Route parent) {
        return parent.getPointsInSequence().getPointOnRoute().iterator();
    }
}
