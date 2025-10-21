package nl.haltedata.netex.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NetexRouteVariantQuayDto {
    private Long id;
    @JsonBackReference
    private NetexRouteVariantDto routeVariant;
    private Long quayIndex;
    private String lineNumber;
    private String quayCode;
    private String quayName;
    private String town;
    private String stopSideCode;
    private String stopPlaceCode;

    public String getName() {
        var sb = new StringBuilder();
        sb.append(quayCode).append(" - ").append(getQuayName());
        if (stopSideCode != null) {
            sb.append(" (").append(stopSideCode).append(")");
        }
        return sb.toString();
    }
}
