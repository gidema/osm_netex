package nl.haltedata.netex.dto;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="netex")
@Getter
@Setter
public class NetexRouteData {
    private String lineNumber;
    @Id
    private String routeId;
    private List<String> quayList;
    private List<String> stopplaceList;
    private Long quayCount;
    private String startQuayCode;
    private String endQuayCode;
    private String startStopplaceCode;
    private String endStopplaceCode;

}
