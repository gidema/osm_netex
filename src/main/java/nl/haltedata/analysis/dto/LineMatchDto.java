package nl.haltedata.analysis.dto;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;
import nl.haltedata.netex.dto.NetexLineDto;
import nl.haltedata.osm.dto.OsmLineDto;

@Getter
@Setter
public class LineMatchDto {
    Long id;
    String network;
    String lineNumber;
    String lineSort;
    String transportMode;
    @JsonManagedReference
    private OsmLineDto osmLine;
    @JsonManagedReference
    private NetexLineDto netexLine;
    @JsonManagedReference
    private List<RouteMatchDto> routeMatches = new LinkedList<>();
}
