package nl.haltedata.osm.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OsmRouteQuayDto {
    private Long id;
    @JsonBackReference
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
