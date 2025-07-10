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
@IdClass(OsmRoutePlatformId.class)
public class DimOsmRoutePlatform {
    @Id
    private Long osmRouteId;
    @Id
    private Long platformIndex;
    private String quayCode;
    private String quayName;
    private String stopSideCode;
    private String areaCode;
}
