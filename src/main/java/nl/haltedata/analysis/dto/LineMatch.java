package nl.haltedata.analysis.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "v_line_match")
public class LineMatch {
    @Id
    Integer id;
    String countryCode;
    String network;
    String transportMode;
    String lineRef;
    String netexLineId;
    Long osmLineId;
    String netexName;
    String osmName;
    String netexColour;
    String osmColour;
    String productCategory;
}
