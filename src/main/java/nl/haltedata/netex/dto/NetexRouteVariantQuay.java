package nl.haltedata.netex.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="v_netex_route_variant_quay")
@IdClass(NetexRouteVariantQuayId.class)
public class NetexRouteVariantQuay {
    @Id
    private Long variantId;
    @Id
    private Long quayIndex;
    private String lineNumber;
    private String quayCode;
    private String quayName;
    private String town;
    private String stopSideCode;
    private String stopPlaceCode;
    private String quayLocationType;

    public String getName() {
        var sb = new StringBuilder();
        sb.append(quayCode).append(" - ").append(getQuayName());
        if (stopSideCode != null) {
            sb.append(" (").append(stopSideCode).append(")");
        }
        return sb.toString();
    }
}
