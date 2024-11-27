package nl.haltedata.chb.osm;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import org.openstreetmap.osmosis.core.domain.v0_6.CommonEntityData;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import nl.haltedata.chb.dto.ChbQuay;
import nl.haltedata.osm.OsmIdFactory;
import nl.haltedata.osm.OsmNodeFactory;

public class ChbQuayNodeFactory implements OsmNodeFactory<ChbQuay> {

    public ChbQuayNodeFactory() {
        super();
    }
    
    @Override
    public Node create(ChbQuay quay, Date timestamp) {
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag("public_transport", "platform"));
        tags.add(new Tag("name", quay.getQuayName()));
        tags.add(new Tag("ref:IFOPT", quay.getQuaycode()));
        quay.getTransportModes().forEach(mode -> {
            switch (mode) {
            case "bus":
            case "troleybus":
                tags.add(new Tag("highway", "bus_stop"));
                tags.add(new Tag("bus", "yes"));
                break;
            case "tram":
                tags.add(new Tag("railway", "platform"));
                tags.add(new Tag("tram", "yes"));
                break;
            case "water":
            case "ferry":
                tags.add(new Tag("ferry", "yes"));
                break;

            
            }
        });
        if (quay.getStopSideCode() != null && !quay.getStopSideCode().isBlank()) {
            tags.add(new Tag("ref", quay.getStopSideCode()));
        }
//        tags.add(new Tag("operator", quay.getOperator()));
//        if (quay.getWheelchairBoarding() != null) {
//            tags.add(new Tag("wheelchair", quay.getWheelchairBoarding() ? "yes" : "no"));            
//        }
        var nodeData = new CommonEntityData(OsmIdFactory.getNodeId(), 0, timestamp, OsmUser.NONE, 0L, tags);
        return new Node(nodeData, quay.getWgsLocation().getY(), quay.getWgsLocation().getX());
    }
}
