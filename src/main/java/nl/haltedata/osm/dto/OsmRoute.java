package nl.haltedata.osm.dto;

import java.util.List;

import jakarta.persistence.Entity;
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

@Entity
@Table(schema="osm_pt")
@Getter
@Setter
@EqualsAndHashCode(of = "osmRouteId")
@NamedEntityGraph(
    name = "osmRouteWithQuays",
    attributeNodes = {
        @NamedAttributeNode("quays"),
    })
public class OsmRoute {
    @Id
    private Long osmRouteId;
    @ManyToOne
    @JoinColumn(name = "osm_line_id")
    private OsmLine osmLine;
    private String name;
    private String transportMode;
    private String routeRef;
    private String operator;
    private String network;
    private String from;
    private String to;
    private String colour;
    @OneToMany(mappedBy ="osmRoute")
    private List<OsmRouteQuay> quays;
}
