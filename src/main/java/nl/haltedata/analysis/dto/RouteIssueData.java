package nl.haltedata.analysis.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RouteIssueData {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
    @SequenceGenerator(name="pk_sequence",sequenceName="route_issue_data_id_seq", allocationSize=1)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "route_match_id")
    private RouteMatch routeMatch;
    private Integer sequence;

    private String message;
    private String[] parameters;
    private String[] lines;
    
    public RouteIssueData() {
        // Default constructor for JPA/Hibernate
    }
}

