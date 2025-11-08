package nl.haltedata.analysis.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import nl.haltedata.netex.dto.NetexRouteVariant;
import nl.haltedata.osm.dto.OsmRoute;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(
        name = "routeMatch-with-issues",
        attributeNodes = {
          @NamedAttributeNode("lineMatch"),
          @NamedAttributeNode("osmRoute"),
          @NamedAttributeNode("netexVariant"),
          @NamedAttributeNode("issues"),
        })
public class RouteMatch {
    @Id
    private Long id;
    private String administrativeZone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private LineMatch lineMatch;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "osm_route_id", referencedColumnName = "osmRouteId")
    private OsmRoute osmRoute;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", referencedColumnName = "id")
    private NetexRouteVariant netexVariant;
    private String matching;
    private Double matchRate;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "routeMatch")
    private List<RouteIssueData> issues = new LinkedList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = IssueStatsConverter.class)
    Map<String, Integer> issueStats;
}
