package nl.haltedata.osm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OsmRouteNetworkIssue {
    private Long osmRouteId;
    private String name;
    private String network;
    private String routeNetwork;
    private String routeOperator;
    private String networkOperator;
}
