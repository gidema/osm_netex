package nl.haltedata.analysis.dto;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.haltedata.netex.dto.NetexLine;
import nl.haltedata.osm.dto.OsmLine;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(
        name = "lineMatch-without-routeMatches",
        attributeNodes = {
          @NamedAttributeNode("networkMatch"),
          @NamedAttributeNode("osmLine"),
          @NamedAttributeNode("netexLine")
        })
@NamedEntityGraph(
        name = "lineMatch-with-routeMatches-issues",
        attributeNodes = {
          @NamedAttributeNode("networkMatch"),
          @NamedAttributeNode("osmLine"),
          @NamedAttributeNode("netexLine"),
          @NamedAttributeNode("routeMatches"),
        }
)
public class LineMatch {
    @Id
    Long id;
    @ManyToOne
    @JoinColumn(name = "administrativeZone", referencedColumnName = "administrativeZone")
    NetworkMatch networkMatch;
//    String network;
    String lineNumber;
    String lineSort;
    @OneToOne
    @JoinColumn(name = "osm_line_id", referencedColumnName = "id")
    private OsmLine osmLine;
    @OneToOne
    @JoinColumn(name = "netex_line_id", referencedColumnName = "id")
    private NetexLine netexLine;
    String productCategory;
    @OneToMany(mappedBy = "lineMatch")
    private List<RouteMatch> routeMatches = new LinkedList<>();
    private String transportMode;

}
