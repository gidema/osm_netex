package nl.haltedata.netex.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "netex")
@Getter
@Setter
public class DimNetexRouteQuay {
    @Id
    private String pointOnRouteId;
    private String routeId;
    private Long quayIndex;
    private String quayCode;
    private String place;
    private String quayName;
    private String stopSideCode;
    private String stopplaceCode;
}
