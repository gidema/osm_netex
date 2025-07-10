package nl.haltedata.osm.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class OsmPrimitiveId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String primitiveType;

    private Long osmPrimitiveId;

    public OsmPrimitiveId() {
        // No Arg constructor to prevent hibernate exception
    }

    public OsmPrimitiveId(String primitiveType, Long osmPrimitiveId) {
        super();
        this.primitiveType = primitiveType;
        this.osmPrimitiveId = osmPrimitiveId;
    }
}