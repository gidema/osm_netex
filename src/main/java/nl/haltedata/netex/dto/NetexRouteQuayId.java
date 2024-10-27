package nl.haltedata.netex.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class NetexRouteQuayId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String routeId;
    private Long quayIndex;

    public NetexRouteQuayId() {
        // No Arg constructor to prevent hibernate exception
    }

    public NetexRouteQuayId(String routeId, Long quayIndex) {
        super();
        this.routeId = routeId;
        this.quayIndex = quayIndex;
    }
}