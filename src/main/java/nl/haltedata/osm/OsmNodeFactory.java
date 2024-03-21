package nl.haltedata.osm;

import java.sql.Date;

import org.openstreetmap.osmosis.core.domain.v0_6.Node;

public interface OsmNodeFactory<T> {
    public Node create(T entity, Date timestamp);
}
