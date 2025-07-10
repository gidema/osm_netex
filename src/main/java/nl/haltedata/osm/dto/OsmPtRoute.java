package nl.haltedata.osm.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OsmPtRoute {
    private Long primitiveId;
    private List<OsmPtQuay> quays;
    private String transportMode;
    private String name;
    private String operator;
    private String ref;
    private String network;
    private String from;
    private String to;
    private String colour;
}