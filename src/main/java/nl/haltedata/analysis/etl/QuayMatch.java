package nl.haltedata.analysis.etl;

import java.util.Objects;

import nl.haltedata.netex.dto.NetexRouteVariantQuay;
import nl.haltedata.osm.dto.OsmRouteQuay;

public class QuayMatch {

    private final OsmRouteQuay osmQuay;
    private final NetexRouteVariantQuay netexQuay;
    private boolean quayCodeMatch = false;
    private boolean stopPlaceMatch = false;
    private boolean nameMatch = false;

    public QuayMatch(OsmRouteQuay osmQuay, NetexRouteVariantQuay netexQuay) {
        super();
        this.osmQuay = osmQuay;
        this.netexQuay = netexQuay;
        if (osmQuay == null || netexQuay == null) return;
        quayCodeMatch = Objects.equals(osmQuay.getQuayCode(), netexQuay.getQuayCode());
        if (isQuayCodeMatch()) {
            stopPlaceMatch = true;
        } else {
            stopPlaceMatch = checkStopPlaceMatch();
        }
        nameMatch = Objects.equals(osmQuay.getName(), netexQuay.getName());
   }

    private boolean checkStopPlaceMatch() {
        if (osmQuay == null || osmQuay.getStopPlace() == null) {
            return false;
        }
        return Objects.equals(osmQuay.getStopPlace(), netexQuay.getStopPlaceCode());
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

    public boolean isStopPlaceMatch() {
        return stopPlaceMatch;
    }

    public void setStopPlaceMatch(boolean stopPlaceMatch) {
        this.stopPlaceMatch = stopPlaceMatch;
    }

    public boolean isNameMatch() {
        return nameMatch;
    }

    public void setNameMatch(boolean nameMatch) {
        this.nameMatch = nameMatch;
    }

    public boolean isMatch() {
        return isQuayCodeMatch() || isStopPlaceMatch() || isNameMatch();
    }
}