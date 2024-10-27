package nl.haltedata.check.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode()
public class DimChbQuay {
    @EqualsAndHashCode.Include
    private String quayCode;
    private String quayName;
    private String stopSideCode;
    private String areaCode;
    private String quayStatus;
    private String quayType;
    private String areaType;
    private Integer bearing;
}
