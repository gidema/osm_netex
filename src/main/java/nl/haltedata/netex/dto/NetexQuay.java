package nl.haltedata.netex.dto;

import org.locationtech.jts.geom.Point;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "netex")
@Getter
@Setter
public class NetexQuay {
    @Id
    private String id;
    private String stopAreaId;
    private String tariffZones;
    private String name;
    private String shortName;
    private String place;
    private String routePointRef;
    private Point rdLocation;
    private Point wgsLocation;
    private String userStopCode;
    private String userStopOwnerCode;
}
