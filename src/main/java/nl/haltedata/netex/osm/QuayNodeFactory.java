package nl.haltedata.netex.osm;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import org.openstreetmap.osmosis.core.domain.v0_6.CommonEntityData;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import nl.haltedata.netex.dto.NetexQuay;
import nl.haltedata.osm.OsmIdFactory;
import nl.haltedata.osm.OsmNodeFactory;

public class QuayNodeFactory implements OsmNodeFactory<NetexQuay> {
    
    public QuayNodeFactory() {
        super();
    }

    @Override
    public Node create(NetexQuay quay, Date timestamp) {
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag("netex:id", quay.getId()));
//        tags.add(new Tag("gtfs:code", quay.getQuayCode()));
        tags.add(new Tag("highway", "bus_stop"));
        tags.add(new Tag("public_transport", "platform"));
        tags.add(new Tag("name", quay.getName()));
//        tags.add(new Tag("ref:IFOPT", quay.getRefIfopt()));
//        tags.add(new Tag("operator", quay.getOperator()));
//        if (quay.getWheelchairBoarding() != null) {
//            tags.add(new Tag("wheelchair", quay.getWheelchairBoarding() ? "yes" : "no"));            
//        }
        var nodeData = new CommonEntityData(OsmIdFactory.getNodeId(), 0, timestamp, OsmUser.NONE, 0L, tags);
        return new Node(nodeData, quay.getWgsLocation().getY(), quay.getWgsLocation().getX());
    }
}
