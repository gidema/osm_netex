package nl.haltedata.analysis.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NamedEntityGraph(
        name = "routeIssueData",
        attributeNodes = {
          @NamedAttributeNode("routeMatch"),
        })
public class RouteIssueData {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
    @SequenceGenerator(name="pk_sequence",sequenceName="route_issue_data_id_seq", allocationSize=1)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_match_id")
    private RouteMatch routeMatch;
    private Integer sequence;

    private String issueType;
    private String[] parameters;
    private String[] lines;
    private String severity;
    
    public RouteIssueData() {
        // Default constructor for JPA/Hibernate
    }
}

