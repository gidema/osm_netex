package nl.haltedata.netex.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NetexRouteVariantDto {
    private Long Id;
    private String lineNumber;
    private String colour;
    private Long quayCount;
    private String lineRef;
    private String directionType;
//    private ChbQuayDto fromQuay;
//    private ChbQuayDto toQuay;
    @JsonManagedReference
    private List<NetexRouteVariantQuayDto> quays;
}
