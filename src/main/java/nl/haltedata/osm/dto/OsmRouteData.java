package nl.haltedata.osm.dto;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="osm_pt")
@Getter
@Setter
public class OsmRouteData {
    private String lineNumber;
    @Id
    private Long osmRouteId;
    private String network;
    private List<String> quayList;
    private List<String> stopplaceList;
    private Integer quayCount;
    private String startQuayCode;
    private String endQuayCode;
    private String startStopplaceCode;
    private String endStopplaceCode;

}
