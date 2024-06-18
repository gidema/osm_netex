package nl.haltedata.gtfs.dto;

import org.locationtech.jts.geom.Point;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(schema = "gtfs")
public class GtfsQuay {
    @Id
    private Long quayId;
    private String quayCode;
    private String refIfopt;
    private String place;
    private String name;
    private String operator;
    private Point coordinates;
    private Long stopAreaId = null;
    private Boolean wheelchairBoarding;
}
