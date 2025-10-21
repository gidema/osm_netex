package nl.haltedata.netex.dto;

import java.util.List;

import org.locationtech.jts.geom.Point;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NetexQuayDto {
    private String id;
    private String stopAreaId;
    private List<String> tariffZones;
    private String name;
    private String shortName;
    private String place;
    private String routePointRef;
    private Point rdLocation;
    private Point wgsLocation;
    private String userStopCode;
    private String userStopOwnerCode;
    private Boolean forBoarding;
    private Boolean forAlighting;
}
