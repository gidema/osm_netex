package nl.haltedata.analysis.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class RouteMatchId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long osmRouteId;
    private Long netexRouteVariantId;

    public RouteMatchId() {
        // No Arg constructor to prevent hibernate exception
    }

    public RouteMatchId(Long osmRouteId, Long netexRouteVariantId) {
        super();
        this.osmRouteId = osmRouteId;
        this.netexRouteVariantId = netexRouteVariantId;
    }
}