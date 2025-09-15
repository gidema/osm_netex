package nl.haltedata.osm.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="osm_pt")
@Getter
@Setter
public class OsmNetwork {
    @Id
    Long id;
    String name;
    String shortName;
    String operator;
    String startDate;
    String endDate;
    String wikidata;
    String note;
    Boolean isConcessie;
}
