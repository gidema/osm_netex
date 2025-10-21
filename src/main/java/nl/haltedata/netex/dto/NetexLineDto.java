package nl.haltedata.netex.dto;

import java.util.List;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NetexLineDto {
    @Id
    private String id;
    private NetexNetworkDto netexNetwork;
    private String name;
    private String brandingRef;
    private String directionType;
    private String transportMode;
    private String publicCode;
//    private String privateCode;
    private String colour;
    private String textColour;
    private boolean mobilityImpairedAccess;
    private String responsibilitySet;
    private String productCategory;
    private String administrativeZone;
    private String lineSort;
    private List<NetexRouteVariantDto> routeVariants;
}
