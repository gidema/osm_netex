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
public class DimOsmRouteQuay {
    @Id
    private Long osmRouteId;
    @Id
    private Long quayIndex;
    private String quayCode;
    private String quayName;
    private String stopSideCode;
    private String areaCode;
}
