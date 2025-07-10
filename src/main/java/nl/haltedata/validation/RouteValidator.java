package nl.haltedata.validation;

import static java.util.Map.entry;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.haltedata.netex.dto.NetexRouteVariant;
import nl.haltedata.netex.dto.NetexRouteVariantQuay;
import nl.haltedata.osm.dto.OsmRoute;
import nl.haltedata.osm.dto.OsmRouteQuay;

/**
 * The QuayMatchFinder iterates over a list of NeTeX quays and a list of OSM quay and tries to find
 * the next QuayMatch.
 * If the first quays on both sides match, a QuayMatch is returned containing the matching quays.
 *  The match is tried on quayCode, areaCode and name in that order. If there is a match on areaCode,
 *   but not on quayCode an minor QuayMatchIssue is created. If there is a match on name, but not on 
 *   quayCode a major QuayIssue is created. If there is an mismatch in the name, a minor QuayIssue is created.
 *
 * If there is no match, an attempt is made to either find the Netex quay by skipping quays in the OSM list or
 * vice-versa.
 *  If a match is found this way, one or more major QuayMatchIssues are created reporting missing/extra quays
 *  in the OSM list. The indexes skip to just after the found match and the match is returned.
 * 
 * If still no match is found, a mismatch is returned containing the first quays on both sides an the
 * indexes are incremented.
 */
//@Component
public class RouteValidator {
    private static I18n i18n = new MockI18n(); 

    private final Map<String, String> colourMap = Map.ofEntries(
        entry("WHITE", "#FFFFFF"),
        entry("LIGHT_GRAY", "#C0C0C0"),
        entry("GRAY", "#808080"),
        entry("DARK_GRAY", "#404040"),
        entry("BLACK", "#000000"),
        entry("RED", "#FF0000"),
        entry("PINK", "#FFAFAF"),
        entry("ORANGE", "#FFC800"),
        entry("YELLOW", "#FFFF00"),
        entry("GREEN", "#00FF00"),
        entry("MAGENTA", "#FF00FF"),
        entry("CYAN", "#00FFFF"),
        entry("BLUE", "#0000FF"));

    private final OsmRoute osmRoute;
    private final NetexRouteVariant netexQuaySequence;
//    private final RouteMatchJosm routeMatch;
    private List<OsmRouteQuay> osmQuays;
    private List<NetexRouteVariantQuay> netexQuays;
    private final List<RouteIssue> matchIssues = new LinkedList<>();
    int osmIndex = 0;
    int netexIndex = 0;
    
    public RouteValidator(OsmRoute osmRoute, NetexRouteVariant netexQuaySequence,
            List<OsmRouteQuay> osmQuays, List<NetexRouteVariantQuay> netexQuays) {
        super();
        this.osmRoute = osmRoute;
        this.netexQuaySequence = netexQuaySequence;
        this.osmQuays = osmQuays;
        this.netexQuays = netexQuays;
    }

    public RouteCheckResults run() {
        checkRouteHeader();
        checkRouteQuays();
        int quayCountDifference = Math.abs(netexQuays.size() - osmQuays.size());
        return new RouteCheckResults(osmRoute, netexQuaySequence, matchIssues, quayCountDifference);
    }

    private void checkRouteHeader() {
        var osmColour = osmRoute.getColour();
        var netexColour = netexQuaySequence.getColour();
        if (osmColour == null && netexColour == null) return;
        if (osmColour == null && netexColour != null) {
            // Ignore Netex colour with value "000000" (black).
            // Some operators use this on every route
            if (!netexColour.equals("000000")) {
                matchIssues.add(new MinorRouteIssue("Missing route colour. Expected #{0}" , netexColour));
            }
        }
        else if (osmColour != null && netexColour == null) {
            matchIssues.add(new MinorRouteIssue(
                "Unexepected route colour ({0}). There is no colour in the Netex data." , osmColour));
        }
        else if (!normalizeColour(osmColour).equals("#" + netexColour)) {
            matchIssues.add(new MinorRouteIssue(
                "Unexepected route colour. Expected {0}, but found {1}.", "#" + netexColour, osmColour));
        }
 
    }
    
    private String normalizeColour(String colour) {
        if (colour == null) return null;
        return colourMap.getOrDefault(colour.toUpperCase(), colour.toUpperCase());
    }

    private void checkRouteQuays() {
        while (osmIndex < osmQuays.size() && netexIndex < netexQuays.size()) {
            nextMatch();
        }
        // Check for trailing Netex quays
        if (netexIndex < netexQuays.size()) {
            matchIssues.add(createMissingQuaysIssue(netexQuays.size() - netexIndex));
        }
        // Check for trailing OSM quays
        if (osmIndex < osmQuays.size()) {
            matchIssues.add(createExtraQuaysIssue(osmQuays.size() - osmIndex));
        }
    }

    private QuayMatch nextMatch() {
        var osmQuay = osmQuays.get(osmIndex);
        var netexQuay = netexQuays.get(netexIndex);
        var match = new QuayMatch(osmQuay, netexQuay);
        if (match.isMatch()) {
            checkMatchIssues(match);
            osmIndex++;
            netexIndex++;
            return match;
        }
        for (int offset = 1 ; offset <= 5 ; offset++) {
            var osmOffsetMatch = getOsmOffsetMatch(netexQuay, offset);
            var netexOffsetMatch = getNetexOsmOffsetMatch(osmQuay, offset);
            // TODO Special case both osmOffsetMatch and netexOffsetMatch means swapped quays
            if (osmOffsetMatch != null && osmOffsetMatch.isMatch()) {
                matchIssues.add(createExtraQuaysIssue(offset));
                checkMatchIssues(osmOffsetMatch);
                osmIndex = osmIndex + offset + 1;
                netexIndex++;
                return osmOffsetMatch;
            }
            if (netexOffsetMatch != null && netexOffsetMatch.isMatch()) {
                matchIssues.add(createMissingQuaysIssue(offset));
                checkMatchIssues(netexOffsetMatch);
                netexIndex = netexIndex + offset + 1;
                osmIndex++;
                return netexOffsetMatch;
            }
        }
        // No match found, not even with offsetting. Report a mismatch issue and return the
        // mismatch;
        matchIssues.add(new MajorRouteIssue(i18n.tr("Deviating quays at position {0}. Expected {1}, but found {2}.",
                osmIndex + 1, netexQuay.getName(), osmQuay.getName())));
        osmIndex++;
        netexIndex++;
        return match;
    }
    
    private RouteIssue createExtraQuaysIssue(int offset) {
        var sb = new StringBuilder();
        if (offset == 1) {
            var extraQuay = osmQuays.get(osmIndex);
            sb.append(i18n.tr("Unexpected quay ({0}) at position {1} in OSM route.\n",
                extraQuay, osmIndex + 1));
        }
        else {
            sb.append(i18n.tr("Unexpected quays found in OSM route:\n"));
            for (int i = 0 ; i<offset; i++) {
                var extraQuay = osmQuays.get(osmIndex + i);
                sb.append(i18n.tr("{0}\n",
                        extraQuay));
            }
        }
        return new MajorRouteIssue(sb.toString());
    }

    
    private RouteIssue createMissingQuaysIssue(int offset) {
        var sb = new StringBuilder();
        if (offset == 1) {
            var extraQuay = netexQuays.get(netexIndex);
            String quayBefore = (osmIndex > 0) ? osmQuays.get(osmIndex - 1).getQuayName() : i18n.tr("start of route");
            String quayAfter = (osmIndex < osmQuays.size()) ? osmQuays.get(osmIndex).getQuayName() : i18n.tr("end of route.");
            sb.append(i18n.tr("Missing quay ({0}) at position {1} in OSM route between {2} and {3}.\n",
                extraQuay.getName(), osmIndex + 1, quayBefore, quayAfter));
        }
        else {
            String quayBefore = (osmIndex > 0) ? osmQuays.get(osmIndex - 1).getQuayName() : i18n.tr("start of route");
            int endIndex = osmIndex + offset;
            String quayAfter = (endIndex < osmQuays.size()) ? osmQuays.get(endIndex).getQuayName() : i18n.tr("end of route.");
            sb.append(i18n.tr("Missing quays found in OSM route between {0} and {1}.:\n", quayBefore, quayAfter));
            for (int i = 0 ; i<offset; i++) {
                var extraQuay = netexQuays.get(netexIndex + i);
                sb.append(i18n.tr("{0} at position {1} in OSM route.\n",
                        extraQuay.getName(), osmIndex + i + 1));
            }
        }
        return new MajorRouteIssue(sb.toString());
    }

    private QuayMatch getOsmOffsetMatch(NetexRouteVariantQuay netexQuay, int offset) {
        if (osmIndex + offset >= osmQuays.size()) return null;
        var osmOffsetQuay = osmQuays.get(osmIndex + offset);
        
        return new QuayMatch(osmOffsetQuay, netexQuay);
    }
    
    private QuayMatch getNetexOsmOffsetMatch(OsmRouteQuay osmQuay, int offset) {
        if (netexIndex + offset >= netexQuays.size()) return null;
        var netexOffsetQuay = netexQuays.get(netexIndex + offset);
        return new QuayMatch(osmQuay, netexOffsetQuay);
    }

    private void checkMatchIssues(QuayMatch match) {
        if (match.isAreaCodeMatch() && ! match.isQuayCodeMatch()) {
            matchIssues.add(createUnexpectedQuayCodeIssue(match, osmIndex));
        }
    }
    
    private static RouteIssue createUnexpectedQuayCodeIssue(QuayMatch match, int position) {
        String expected = match.getNetexQuay().getName();
        String found = match.getOsmQuay().getName();
        return new MinorRouteIssue("Unexpected quay code for quay {0} at position {1}. Expected {2}, but found {3}.",
           match.getOsmQuay().getName(), position + 1, expected, found);
    }
    

}
