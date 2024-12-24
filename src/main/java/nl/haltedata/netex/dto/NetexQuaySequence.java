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
public class NetexQuaySequence {
    @Id
    private Long Id;
    private String lineNumber;
    private List<String> quayList;
    private List<String> stopPlaceList;
    private Long quayCount;
    private String startQuayCode;
    private String endQuayCode;
    private String startStopPlaceCode;
    private String endStopPlaceCode;
    private String lineRef;
    private List<String> route_refs;
    private String direction_type;
}
