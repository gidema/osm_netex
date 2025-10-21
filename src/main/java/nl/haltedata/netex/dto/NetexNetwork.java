package nl.haltedata.netex.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "netex")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(
        name = "netexNetworkWithLines",
        attributeNodes = {
                @NamedAttributeNode("lines"),
        })
public class NetexNetwork {
    @Id
    private String id;
    private String name;
    private String shortName;
    private String description;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String groupOfLinesType;
    private String authorityRef;
    private String fileSetId;
    private String administrativeZone;
    @OneToMany(mappedBy = "netexNetwork")
    private List<NetexLine> lines;
}
