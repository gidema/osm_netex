package nl.haltedata.netex.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(schema="netex")
@IdClass(NetexRouteQuayId.class)
public class NetexRouteQuay {
    @Id
    String routeId;
    @Id
    Long quayIndex;
    String lineNumber;
    String quayCode;
    String stopSideCode;
    String stopPlaceCode;
}
