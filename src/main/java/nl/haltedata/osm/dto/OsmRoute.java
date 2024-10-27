package nl.haltedata.osm.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="osm_pt")
@Getter
@Setter
public class OsmRoute {
    @Id
    private Long osmRouteId;
    private Long osmLineId;
    private String name;
    private String transportMode;
    private String routeRef;
    private String operator;
    private String network;
    private String from;
    private String to;
    private String colour;
}
