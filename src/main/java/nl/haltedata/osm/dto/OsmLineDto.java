package nl.haltedata.osm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OsmLineDto {
    private Long id;
    @JsonBackReference
    private List<OsmNetworkDto> osmNetworks;
    private String osmTransportMode;
    private String netexTransportMode;
    private String name;
    private String operator;
    private String lineNumber;
    private String network;
    private String colour;
    private Boolean isDisused;
    private String lineSort;
    @JsonManagedReference
    private List<OsmRouteDto> routes;
}
