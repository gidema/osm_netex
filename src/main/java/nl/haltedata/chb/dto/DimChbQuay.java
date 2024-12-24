package nl.haltedata.chb.dto;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "chb")
public class DimChbQuay {
    @Id
    private String quayCode;
    private String quayName;
    private String stopSideCode;
    private String areaCode;
    private String quayStatus;
    private String quayType;
    private String areaType;
    private Integer bearing;
    private String place;
    private List<String> transportModes;
}
