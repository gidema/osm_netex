package nl.haltedata.osm.dto;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="osm_pt", name="osm_line")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(
        name = "osmLineWithNetworksAndRoutes",
        attributeNodes = {
//                @NamedAttributeNode("osmNetworks"),
                @NamedAttributeNode("routes"),
        })
public class OsmLine {
    @Id
    private Long id;
    @ManyToMany(mappedBy ="osmLines")
    private List<OsmNetwork> osmNetworks;
    private String osmTransportMode;
    private String netexTransportMode;
    private String name;
    private String operator;
    private String lineNumber;
    private String network;
    private String colour;
    private Boolean isDisused;
    private String lineSort;
    @OneToMany(mappedBy ="osmLine")
    private List<OsmRoute> routes;
}
