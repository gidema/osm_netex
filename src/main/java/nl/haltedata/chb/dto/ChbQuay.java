package nl.haltedata.chb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import org.locationtech.jts.geom.Point;

@Entity
@Table(schema = "chb")
@Getter
@Setter
public class ChbQuay {

    @Id
    private String iD;
    private String stopPlaceId;
    private String stopPlaceName;
    private String stopPlaceLongName;
    private Boolean onlygetout;
    private String quaycode;
    private LocalDateTime validfrom;
    private LocalDateTime mutationdate;
    private String quayType;
    private String quayStatus;
    private Point rdLocation;
    private Point wgsLocation;
    private Integer bearing;
    private String town;
    private String level;
    private String street;
    private String location;  
}