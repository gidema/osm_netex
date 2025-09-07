package nl.haltedata.analysis.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NetworkMatch {
    @Id
    Long id;
    String name;
    String dovaId;
    String dovaName;
    String shortName;
    Long osmId;
    String osmName;
    String netexId;
    String netexName;
}
