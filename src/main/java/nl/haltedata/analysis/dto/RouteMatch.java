package nl.haltedata.analysis.dto;

import java.util.Comparator;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "v_route_match")
public class RouteMatch {
    @Id
    private Long id;
    private Long lineId;
    private String lineNumber;
    private String lineSort;
    private Long osmRouteId;
    private Long netexVariantId;
    private String network;
    private Double matchRate;
    private String matching;
    private String netexLineNumber;
    private String netexName;
    private String directionType;
    private String osmName;
    private String osmTransportMode;
    private String osmLineNumber;
    private String from;
    private String to;
    
    public static class LineSortComparator implements Comparator<RouteMatch> {
        @Override
        public int compare(RouteMatch rm1, RouteMatch rm2) {
            var lineSort1 = Objects.requireNonNullElse(rm1.getLineSort(), "");
            var lineSort2 = Objects.requireNonNullElse(rm2.getLineSort(), "");
            return lineSort1.compareTo(lineSort2);
        }
    }
}
