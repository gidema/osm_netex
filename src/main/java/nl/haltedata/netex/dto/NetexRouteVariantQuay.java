package nl.haltedata.netex.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.haltedata.osm.dto.OsmLine;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(schema="netex")
public class NetexRouteVariantQuay {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "variant_id")
    private NetexRouteVariant routeVariant;
    private Long quayIndex;
    private String lineNumber;
    private String quayCode;
    private String quayName;
    private String town;
    private String stopSideCode;
    private String stopPlaceCode;

//    public String getName() {
//        var sb = new StringBuilder();
//        sb.append(quayCode).append(" - ").append(getQuayName());
//        if (stopSideCode != null) {
//            sb.append(" (").append(stopSideCode).append(")");
//        }
//        return sb.toString();
//    }
}
