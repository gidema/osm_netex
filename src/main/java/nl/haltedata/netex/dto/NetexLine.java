package nl.haltedata.netex.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "netex")
@Getter
@Setter
public class NetexLine {
    @Id
    private String id;
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
}
