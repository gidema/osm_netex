package nl.haltedata.osm.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="osm_pt")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class OsmRouteIssue {
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "osm_route_id")
    private OsmRoute osmRoute;
    private String issue_type;
    private String severity;
}
