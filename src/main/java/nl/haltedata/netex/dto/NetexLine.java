package nl.haltedata.netex.dto;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.haltedata.osm.dto.OsmLine;

@Entity
@Table(schema = "netex")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(
        name = "netexLine-with-routeVariants",
        attributeNodes = {
          @NamedAttributeNode("routeVariants"),
        })
public class NetexLine {
    @Id
    private String id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrative_zone", referencedColumnName = "administrativeZone")
    private NetexNetwork netexNetwork;
    private String brandingRef;
    private String directionType;
    private String transportMode;
    private String publicCode;
    private String colour;
    private String textColour;
    private boolean mobilityImpairedAccess;
    private String responsibilitySet;
    private String productCategory;
    private String lineSort;
    @OneToMany(mappedBy = "netexLine")
    private List<NetexRouteVariant> routeVariants;
}
