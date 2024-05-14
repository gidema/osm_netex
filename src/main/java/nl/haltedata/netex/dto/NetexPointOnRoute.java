package nl.haltedata.netex.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="netex")
@Getter
@Setter
public class NetexPointOnRoute {
    @Id
    private String pointOnRouteId;
    private String routeId;
    private Integer sequence;
    private String routePointRef;
}
