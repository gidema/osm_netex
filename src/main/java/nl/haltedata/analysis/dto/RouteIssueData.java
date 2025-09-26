package nl.haltedata.analysis.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;

@Entity
@Getter
public class RouteIssueData {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
    @SequenceGenerator(name="pk_sequence",sequenceName="route_issue_data_id_seq", allocationSize=1)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "route_match_id")
    @JsonBackReference
    private RouteMatch routeMatch;
    private Integer sequence;

    private String message;
    private String[] parameters;
    
    public RouteIssueData() {
        // Default constructor for JPA/Hibernate
    }
    
    public RouteIssueData(RouteMatch routeMatch, Integer sequence, String message, String... parameters) {
        super();
        this.routeMatch = routeMatch;
        this.sequence = sequence;
        this.message = message;
        this.parameters = parameters;
    }
}

