package nl.haltedata.netex.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "netex")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class NetexRoute {
    @Id
    private String id;
    @ManyToOne()
    @JoinColumn(name = "variant_id")
    private NetexRouteVariant routeVariant;
    private String lineRef;
    private String name;
//    private String administrativeZone;
//    private String brandingRef;
    private String directionType;
//    private String transportMode;
//    private String publicCode;
//    private String privateCode;
//    private String colour;
//    private String textColour;
//    private boolean mobilityImpairedAccess;
}
