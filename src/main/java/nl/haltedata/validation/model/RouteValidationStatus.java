package nl.haltedata.validation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RouteValidationStatus {
    @Id
    private Long osmRouteId;
    private String network;
    private String line;
    private String name;
    private String transportMode;
    private String matching;
    private String issue;
}
