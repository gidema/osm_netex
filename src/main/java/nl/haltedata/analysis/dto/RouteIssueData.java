package nl.haltedata.analysis.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;

@Entity
@Getter
@IdClass(RouteIssueId.class)
public class RouteIssueData {
    @Id
    private Long routeMatchId;
    @Id
    private Integer sequence;
    private String message;
    private String[] parameters;
    
    public RouteIssueData() {
        // Default constructor for JPA/Hibernate
    }
    
    public RouteIssueData(Long routeMatchId, Integer sequence, String message, String... parameters) {
        super();
        this.routeMatchId = routeMatchId;
        this.sequence = sequence;
        this.message = message;
        this.parameters = parameters;
    }
}

