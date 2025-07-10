package nl.haltedata.compare.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//@IdClass(RouteMatchId.class)
public class RouteMatchJosm {
    @Id
    private Long osmRouteId;
    private String osmRouteName;
//    @Id
    private Long netexRouteVariantId;
    private String netexLineName;
    private String netexDirection;
    private Integer quayCountDifference;
    private Boolean startAreaMatch;
    private Boolean endAreaMatch;
}
