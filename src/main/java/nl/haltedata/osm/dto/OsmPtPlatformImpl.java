package nl.haltedata.osm.dto;

import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Entity
//@Table(schema="osm_pt", name="OsmPtPlatform")
@IdClass(OsmPrimitiveId.class)
public class OsmPtPlatformImpl implements OsmPtPlatform {
    @Id
    Long osmPrimitiveId;
    @Id
    String primitiveType;
    String name;
    Boolean isBus;
    String quayCode;
    String areaCode;
    String stopSideCode;
    String note;
    
    @Override
    public String toString() {
        return quayCode + " (" + name + 
            stopSideCode == null ? "" : " - " + stopSideCode +
            ")";
    }
}
