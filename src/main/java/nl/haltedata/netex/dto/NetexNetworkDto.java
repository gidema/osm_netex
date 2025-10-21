package nl.haltedata.netex.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NetexNetworkDto {
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
    private List<NetexLineDto> lines;
}
