package nl.haltedata.analysis.dto;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;
import nl.haltedata.netex.dto.NetexRouteVariantDto;
import nl.haltedata.osm.dto.OsmRouteDto;

@Getter
@Setter
public class RouteMatchDto {
    private Long id;
    private String administrativeZone;
    @JsonBackReference
    private LineMatchDto lineMatch;
    @JsonManagedReference
    private OsmRouteDto osmRoute;
    @JsonManagedReference
    private NetexRouteVariantDto netexVariant;
    private String matching;
    private Double matchRate;
    @JsonManagedReference
    private List<RouteIssueDataDto> issues = new LinkedList<>();
}
