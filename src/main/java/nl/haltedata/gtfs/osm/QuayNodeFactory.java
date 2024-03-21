package nl.haltedata.gtfs.osm;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import org.openstreetmap.osmosis.core.domain.v0_6.CommonEntityData;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import nl.haltedata.gtfs.dto.GtfsQuay;
import nl.haltedata.osm.OsmIdFactory;
import nl.haltedata.osm.OsmNodeFactory;

public class QuayNodeFactory implements OsmNodeFactory<GtfsQuay> {
    
    public QuayNodeFactory() {
        super();
    }

    @Override
    public Node create(GtfsQuay quay, Date timestamp) {
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag("gtfs:id", quay.getQuayId().toString()));
        tags.add(new Tag("gtfs:code", quay.getQuayCode()));
        tags.add(new Tag("highway", "bus_stop"));
        tags.add(new Tag("public_transport", "platform"));
        tags.add(new Tag("name", quay.getName()));
        tags.add(new Tag("ref:IFOPT", "NL:Q:" + quay.getRefIfopt()));
        tags.add(new Tag("operator", quay.getOperator()));
        if (quay.getWheelchairBoarding() != null) {
            tags.add(new Tag("wheelchair", quay.getWheelchairBoarding() ? "yes" : "no"));            
        }
        var nodeData = new CommonEntityData(OsmIdFactory.getNodeId(), 0, timestamp, OsmUser.NONE, 0L, tags);
        return new Node(nodeData, quay.getCoordinates().getY(), quay.getCoordinates().getX());
    }
}
