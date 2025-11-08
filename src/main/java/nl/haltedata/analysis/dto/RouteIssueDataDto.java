package nl.haltedata.analysis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RouteIssueDataDto {
    private Long id;
//    @JsonBackReference
    @JsonIgnoreProperties({"issues"})
    private RouteMatchDto routeMatch;
    private Integer sequence;
    private String issueType;
    private String[] parameters;
    private String[] lines;
    private String severity;

    public RouteIssueDataDto(RouteMatchDto routeMatch, Integer sequence, String issueType, String[] parameters, String[] lines, String severity) {
        super();
        this.routeMatch = routeMatch;
        this.sequence = sequence;
        this.issueType = issueType;
        this.parameters = parameters;
        this.lines = lines;
        this.severity = severity;
    }
}

