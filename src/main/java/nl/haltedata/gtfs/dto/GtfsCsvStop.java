package nl.haltedata.gtfs.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "stopId")
public class GtfsCsvStop {
    private String stopId;
    private String stopCode;
    private String stopName;
    private Double stopLat;
    private Double stopLon;
    private Integer locationType;
    private String parentStation;
    private String stopTimezone;
    private Integer wheelchairBoarding;
    private String platformCode;
    private String zoneId;
}
