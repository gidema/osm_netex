package nl.haltedata.analysis.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RouteIssueDataDto {
    private Long id;
    @JsonBackReference
    private RouteMatchDto routeMatch;
    private Integer sequence;
    private String message;
    private String[] parameters;
    private String[] lines;

    public RouteIssueDataDto(RouteMatchDto routeMatch, Integer sequence, String message, String[] parameters, String[] lines) {
        super();
        this.routeMatch = routeMatch;
        this.sequence = sequence;
        this.message = message;
        this.parameters = parameters;
        this.lines = lines;
    }
}

