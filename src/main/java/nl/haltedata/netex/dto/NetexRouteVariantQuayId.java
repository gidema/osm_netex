package nl.haltedata.netex.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class NetexRouteVariantQuayId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long variantId;
    private Long quayIndex;

    public NetexRouteVariantQuayId() {
        // No Arg constructor to prevent hibernate exception
    }

    public NetexRouteVariantQuayId(Long variantId, Long quayIndex) {
        super();
        this.variantId = variantId;
        this.quayIndex = quayIndex;
    }
}