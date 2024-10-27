package nl.haltedata.compare.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class RouteMatchId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long osmRouteId;
    private String netexRouteId;

    public RouteMatchId() {
        // No Arg constructor to prevent hibernate exception
    }

    public RouteMatchId(Long osmRouteId, String netexRouteId) {
        super();
        this.osmRouteId = osmRouteId;
        this.netexRouteId = netexRouteId;
    }
}