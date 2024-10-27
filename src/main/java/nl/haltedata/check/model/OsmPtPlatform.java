package nl.haltedata.check.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OsmPtPlatform {
    Long osmPrimitiveId;
    PrimitiveType primitiveType;
    String name;
    Boolean isBus;
    String stopSideCode;
    String refIfopt;
    String note;
}
