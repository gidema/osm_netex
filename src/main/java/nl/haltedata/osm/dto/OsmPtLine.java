package nl.haltedata.osm.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="osm_pt", name="osm_route_master")
@Getter
@Setter
public class OsmPtLine {
    @Id
    private Long osmRouteMasterId;
    private String name;
    private String network;
    private String transportMode;
    private String operator;
    private String routeRef;
    private String colour;

}
