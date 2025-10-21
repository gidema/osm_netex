package nl.haltedata.osm.dto;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="osm_pt")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(
        name = "osmNetworkWithLines",
        attributeNodes = {
                @NamedAttributeNode("osmLines"),
        })
public class OsmNetwork {
    @Id
    Long id;
    String name;
    String shortName;
    String operator;
    String startDate;
    String endDate;
    String wikidata;
    String note;
    Boolean isConcessie;
    @ManyToMany
    @JoinTable(schema="osm_pt",
            name="osm_line_in_network",
            joinColumns = @JoinColumn(name = "network_id"),
            inverseJoinColumns = @JoinColumn(name = "line_id"))
    List<OsmLine> osmLines;
}
