package nl.haltedata.chb.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "chb")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ChbQuay {

    @Id
    private String id;
    private String stopPlaceId;
    private String stopPlaceName;
    private String stopPlaceLongName;
    private List<String> transportModes;
    private Boolean onlygetout;
    private String quayCode;
    private String quayName;
    private String stopSideCode;
    private LocalDateTime validfrom;
    private LocalDateTime mutationdate;
    private String quayType;
    private String quayStatus;
//    private Point rdLocation;
//    private Point wgsLocation;
    private Integer bearing;
    private String town;
    private String level;
    private String street;
    private String location;  
}