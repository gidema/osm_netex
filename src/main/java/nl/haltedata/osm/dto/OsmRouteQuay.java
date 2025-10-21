package nl.haltedata.osm.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "osm_pt")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class OsmRouteQuay {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "osm_route_id", referencedColumnName = "osmRouteId")
    private OsmRoute osmRoute;
    private Long quayIndex;
    private Long platformPointId;
    private Long platformAreaId;
    private String quayCode;
    private String quayName;
    private String stopPlace;
    private String stopSideCode;
    
    public String getName() {
        var sb = new StringBuilder();
        sb.append(quayCode).append(" - ").append(getQuayName());
        if (stopSideCode != null) {
            sb.append(" (").append(stopSideCode).append(")");
        }
        return sb.toString();
    }
}
