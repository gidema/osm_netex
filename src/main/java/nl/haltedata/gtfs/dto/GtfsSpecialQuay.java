package nl.haltedata.gtfs.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity()
@Table(schema = "gtfs")
@Getter
@Setter
public class GtfsSpecialQuay {
    @Id
    private Long gtfsId;
    private String operator;
}
