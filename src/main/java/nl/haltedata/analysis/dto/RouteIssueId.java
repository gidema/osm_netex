package nl.haltedata.analysis.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class RouteIssueId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long routeMatchId;

    private Integer sequence;

    public RouteIssueId() {
        // No Arg constructor to prevent hibernate exception
    }

    public RouteIssueId(Long routeMatchId, Integer sequence) {
        super();
        this.routeMatchId = routeMatchId;
        this.sequence = sequence;
    }
}