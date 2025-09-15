package nl.haltedata.netex.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "netex")
@Getter
@Setter
public class NetexNetwork {
    @Id
    private String id;
    private String name;
    private String shortName;
    private String description;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String groupOfLinesType;
    private String authorityRef;
    private String fileSetId;
    private String administrativeZone;
}
