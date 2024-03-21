package nl.haltedata.osm;

import java.util.concurrent.atomic.AtomicLong;

public class OsmIdFactory {
    private final static AtomicLong nodeId = new AtomicLong(-1);
    private final static AtomicLong wayId = new AtomicLong(-1);
    private final static AtomicLong relationId = new AtomicLong(-1);

    public static Long getNodeId() {
        return nodeId.getAndDecrement();
    }

    public static Long getWayId() {
        return wayId.getAndDecrement();
    }

    public static Long getRelationId() {
        return relationId.getAndDecrement();
    }
}
