package nl.haltedata.osm.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="osm_pt", name="osm_line")
@Getter
@Setter
public class OsmPtLine {
    @Id
    private Long id;
    private String osmTransportMode;
    private String netexTransportMode;
    private String name;
    private String operator;
    private String lineNumber;
    private String network;
    private String colour;
    private Boolean is_disused;
    private String administrative_zone;
    private String line_sort;

}
