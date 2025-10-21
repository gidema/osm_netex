package nl.haltedata.osm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OsmRouteDto {
    private Long osmRouteId;
    @JsonBackReference
    private OsmLineDto osmLine;
    private String name;
    private String transportMode;
    private String routeRef;
    private String operator;
    private String network;
    private String from;
    private String to;
    private String colour;
    @JsonManagedReference
    private List<OsmRouteQuayDto> quays;
}
