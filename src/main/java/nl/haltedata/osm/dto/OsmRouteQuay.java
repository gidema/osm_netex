package nl.haltedata.osm.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "osm_pt")
@Getter
@Setter
@IdClass(OsmRouteQuayId.class)
public class OsmRouteQuay {
    @Id
    private Long osmRouteId;
    @Id
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
