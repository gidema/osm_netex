package nl.haltedata.osm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OsmNetworkDto {
    Long id;
    String name;
    String shortName;
    String operator;
    String startDate;
    String endDate;
    String wikidata;
    String note;
    Boolean isConcessie;
    @JsonManagedReference
    List<OsmLineDto> lines;
}
