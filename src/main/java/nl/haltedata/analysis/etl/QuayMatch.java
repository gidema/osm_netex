package nl.haltedata.analysis.etl;

import java.util.Objects;

import nl.haltedata.netex.dto.NetexRouteVariantQuay;
import nl.haltedata.osm.dto.OsmRouteQuay;

public class QuayMatch {

    private final OsmRouteQuay osmQuay;
    private final NetexRouteVariantQuay netexQuay;
    private boolean quayCodeMatch = false;
    private boolean areaCodeMatch = false;
    private boolean nameMatch = false;

    public QuayMatch(OsmRouteQuay osmQuay, NetexRouteVariantQuay netexQuay) {
        super();
        this.osmQuay = osmQuay;
        this.netexQuay = netexQuay;
        if (osmQuay == null || netexQuay == null) return;
        quayCodeMatch = Objects.equals(osmQuay.getQuayCode(), netexQuay.getQuayCode());
        if (isQuayCodeMatch()) {
            areaCodeMatch = true;
        } else {
            areaCodeMatch = checkAreaCodeMatch();
        }
        nameMatch = Objects.equals(osmQuay.getName(), netexQuay.getName());
   }

    private boolean checkAreaCodeMatch() {
        if (osmQuay == null || osmQuay.getAreaCode() == null) {
            return false;
        }
        return Objects.equals(osmQuay.getAreaCode(), netexQuay.getStopPlaceCode());
    }

    public OsmRouteQuay getOsmQuay() {
        return osmQuay;
    }

    public NetexRouteVariantQuay getNetexQuay() {
        return netexQuay;
    }

    public boolean isQuayCodeMatch() {
        return quayCodeMatch;
    }

    public void setQuayCodeMatch(boolean quayCodeMatch) {
        this.quayCodeMatch = quayCodeMatch;
    }

    public boolean isAreaCodeMatch() {
        return areaCodeMatch;
    }

    public void setAreaCodeMatch(boolean areaCodeMatch) {
        this.areaCodeMatch = areaCodeMatch;
    }

    public boolean isNameMatch() {
        return nameMatch;
    }

    public void setNameMatch(boolean nameMatch) {
        this.nameMatch = nameMatch;
    }

    public boolean isMatch() {
        return isQuayCodeMatch() || isAreaCodeMatch() || isNameMatch();
    }
}