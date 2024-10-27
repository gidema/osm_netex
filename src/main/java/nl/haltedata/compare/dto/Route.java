package nl.haltedata.compare.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "v_all_routes")
public class Route {
    @Id
    private Long id;
    private Long lineId;
    private Long osmRouteId;
    private Long netexRouteId;
    private String matching;
    private String netexLineNumber;
    private String netexName;
    private String directionType;
    private String osmName;
    private String osmTransportMode;
    private String osmLineNumber;
    private String from;
    private String to;
}
