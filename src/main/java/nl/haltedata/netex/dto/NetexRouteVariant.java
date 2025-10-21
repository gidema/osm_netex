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
import nl.haltedata.chb.dto.ChbQuay;

@Entity
@Table(schema="netex")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(
        name = "netexRouteVariant",
        attributeNodes = {
            @NamedAttributeNode("netexLine"),
            @NamedAttributeNode("quays"),
        })
public class NetexRouteVariant {
    @Id
    private Long id;
    private String lineNumber;
    private String colour;
    private Long quayCount;
    private String directionType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_ref")
    private NetexLine netexLine;
    @OneToMany(mappedBy = "routeVariant", fetch = FetchType.LAZY)
    private List<NetexRoute> netexRoutes;
    @OneToMany(mappedBy = "routeVariant")
    private List<NetexRouteVariantQuay> quays;
}
