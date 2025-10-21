package nl.haltedata.analysis.dto;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import nl.haltedata.netex.dto.NetexNetwork;
import nl.haltedata.osm.dto.OsmNetwork;

@Entity
@Getter
@Setter
@NamedEntityGraph(
        name = "networkMatch-list",
        attributeNodes = {
          @NamedAttributeNode("osmNetwork"),
          @NamedAttributeNode("netexNetwork"),
//          @NamedAttributeNode("lineMatches"),
        })
public class NetworkMatch {
    @Id
    String id;
    String administrativeZone;
    String name;
    String shortName;
    @OneToOne
//    @JoinColumn(name="osm_id", unique=true, nullable=true, updatable=false)
    @JoinColumn(name="osm_id", nullable=true, updatable=false)
    OsmNetwork osmNetwork;
    @OneToOne
    @JoinColumn(name="netex_id", unique=true, nullable=true, updatable=false)
    NetexNetwork netexNetwork;
    @OneToMany(mappedBy = "networkMatch")
    List<LineMatch> lineMatches;
}
