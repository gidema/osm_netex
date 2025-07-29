package nl.haltedata.analysis.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import nl.haltedata.util.I18n;

@Entity
@Getter
public class RouteIssueData {
    @Id
    private Integer id;
    private Long routeMatchId;
    private String message;
//    @Type(JpaStringArrayType.class)
    private String[] parameters;
    
    public RouteIssueData() {
        // Default constructor for JPA/Hibernate
    }
    
    public RouteIssueData(Integer id, Long routeMatchId, String message, String... parameters) {
        super();
        this.id = id;
        this.routeMatchId = routeMatchId;
        this.message = message;
        this.parameters = parameters;
    }

    public String getMessage(I18n i18n) {
        return i18n.tr(message, parameters);
    }
}

