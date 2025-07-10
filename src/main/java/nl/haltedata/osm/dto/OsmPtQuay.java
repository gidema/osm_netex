package nl.haltedata.osm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OsmPtQuay {
    private String quayCode;
    private String areaCode;
    private OsmPtPlatform platformNode;
    private OsmPtPlatform platformWay;

    public OsmPtQuay(String quayCode) {
        this.quayCode = quayCode;
    }
    
    public String getName() {
        if (platformNode != null) return platformNode.getName();
        if (platformWay != null) return platformWay.getName();
        return null;
    }
    
    public String getStopSideCode() {
        if (platformNode != null) return platformNode.getStopSideCode();
        if (platformWay != null) return platformWay.getStopSideCode();
        return null;
    }
    
    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(quayCode).append(" - ").append(getName());
        var stopsideCode = getStopSideCode();
        if (stopsideCode != null) {
            sb.append(" (").append(stopsideCode).append(")");
        }
        return sb.toString();
    }

}
