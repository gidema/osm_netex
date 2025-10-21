package nl.haltedata.analysis.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;
import nl.haltedata.netex.dto.NetexNetworkDto;
import nl.haltedata.osm.dto.OsmNetworkDto;

@Getter
@Setter
public class NetworkMatchDto {
    String id;
    String administrativeZone;
    String name;
    String shortName;
    @JsonManagedReference
    OsmNetworkDto osmNetwork;
    @JsonManagedReference
    NetexNetworkDto netexNetwork;
    @JsonManagedReference
    List<LineMatchDto> lineMatches;
}
