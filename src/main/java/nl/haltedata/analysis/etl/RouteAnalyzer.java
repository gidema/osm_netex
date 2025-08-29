package nl.haltedata.analysis.etl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.haltedata.analysis.RouteAnalysis;
import nl.haltedata.analysis.dto.RouteIssueData;
import nl.haltedata.analysis.dto.RouteIssueDataRepository;
import nl.haltedata.analysis.dto.RouteMatch;
import nl.haltedata.analysis.dto.RouteMatchRepository;
import nl.haltedata.netex.dto.NetexRouteVariant;
import nl.haltedata.netex.dto.NetexRouteVariantQuay;
import nl.haltedata.netex.dto.NetexRouteVariantQuayRepository;
import nl.haltedata.netex.dto.NetexRouteVariantRepository;
import nl.haltedata.osm.dto.OsmRoute;
import nl.haltedata.osm.dto.OsmRouteQuay;
import nl.haltedata.osm.dto.OsmRouteQuayRepository;
import nl.haltedata.osm.dto.OsmRouteRepository;

/**
 * The RouteAnalyzer iterates over a list of NeTeX quays and a list of OSM quay and tries to find
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
 * If still no match is found, a mismatch is returned containing the first quays on both sides and the
 * indexes are incremented.
 */
@Component
public class RouteAnalyzer {
    @Inject
    OsmRouteQuayRepository osmRouteQuayRepository;
    
    @Inject
    private NetexRouteVariantQuayRepository netexRouteVariantQuayRepository;
    
    @Inject
    private RouteMatchRepository routeMatchRepository;
    
    @Inject
    private OsmRouteRepository osmRouteRepository;
    
    @Inject
    private NetexRouteVariantRepository netexRouteVariantRepository;
    
    @Inject
    private RouteIssueDataRepository routeIssueDataRepository;
    
    public RouteAnalysis analize(Long routeMatchId) {
        var rm = routeMatchRepository.findById(routeMatchId).get();
        return analize(rm);
    }

    public RouteAnalysis analize(@SuppressWarnings("exports") RouteMatch match) {
        var analizer = new Analizer();
        return analizer.analize(match);
    }
    
    class Analizer {
        private RouteMatch routeMatch;
        private OsmRoute osmRoute;
        private NetexRouteVariant netexRouteVariant;
        private List<OsmRouteQuay> osmQuays;
        private List<NetexRouteVariantQuay> netexQuays;
        private List<QuayMatch> quayMatches = new LinkedList<>();
        private final List<RouteIssueData> issues = new LinkedList<>();
        int osmIndex = 0;
        int netexIndex = 0;

        public RouteAnalysis analize(RouteMatch match) {
            this.routeMatch = match;
            osmRoute = osmRouteRepository.findById(routeMatch.getOsmRouteId()).get();
            netexRouteVariant = netexRouteVariantRepository.findById(routeMatch.getNetexVariantId()).get();
            checkRouteHeader();
            osmQuays = new ArrayList<>(osmRouteQuayRepository.findByOsmRouteId(osmRoute.getOsmRouteId()));
            netexQuays = new ArrayList<>(netexRouteVariantQuayRepository.
                findByVariantId(netexRouteVariant.getId()));
            checkRouteQuays();
            int quayCountDifference = Math.abs(netexQuays.size() - osmQuays.size());
            routeIssueDataRepository.saveAll(issues);
            return new RouteAnalysis(routeMatch, osmRoute, issues, quayCountDifference);
        }
    
        private void checkRouteHeader() {
            var osmColour = osmRoute.getColour();
            var netexColour = netexRouteVariant.getColour();
            if (osmColour == null && netexColour == null) return;
            if (osmColour == null && netexColour != null) {
                // Ignore Netex colour with value "000000" (black).
                // Some operators use this on every route
                if (!netexColour.equals("000000")) {
                    addIssue("MissingRouteColour", "#" + netexColour);
                }
            }
            else if (osmColour != null && netexColour == null) {
                addIssue("UnexpectedRouteColour1" , osmColour);
            }
            else if (!ColourMap.normalizeColour(osmColour).equals("#" + netexColour)) {
                addIssue("UnexpectedRouteColour2", "#" + netexColour, osmColour);
            }
        }
        
        private void checkRouteQuays() {
            while (osmIndex < osmQuays.size() && netexIndex < netexQuays.size()) {
                var osmQuay = osmQuays.get(osmIndex);
                var netexQuay = netexQuays.get(netexIndex);
                if (quaysMatch(osmQuay, netexQuay)) {
                    var quayMatch = new QuayMatch(osmQuay, netexQuay);
                    analizeQuayMatch(quayMatch);
                    quayMatches.add(quayMatch);
                    osmIndex++;
                    netexIndex++;
                    continue;
                }
                var netexOffset = findNetexOffset(osmQuay);
                var osmOffset = findOsmOffset(netexQuay);
                if (netexOffset == 1 && osmOffset == 1) {
                    addIssue("DifferentQuay", Integer.toString(osmIndex + 1), netexQuay.getName(), osmQuay.getName());
                    osmIndex++;
                    netexIndex++;
                }
                // Extra quays in the OSM route
                else if (netexOffset == -1 && osmOffset > 0) {
                    var sb = new StringBuilder();
                    var location = osmIndex + 1;
                    for (int i =0 ; i < osmOffset; i++) {
                        osmQuay = osmQuays.get(osmIndex++);
                        quayMatches.add(new QuayMatch(osmQuay, null));
                        sb.append("\n").append(osmQuay.getName());
                    }
                    addIssue("UnexpectedQuays", Integer.toString(osmOffset), Integer.toString(location), sb.toString());
                    continue;
                }
                // Missing quays in the OSM route
                else if (osmOffset == -1 && netexOffset > 0) {
                    String quayBefore = (osmIndex > 0 && (osmIndex - 1) < osmQuays.size()) ? osmQuays.get(osmIndex - 1).getName() : "start of route";
                    String quayAfter = (osmIndex < osmQuays.size()) ? osmQuays.get(osmIndex).getName() : "end of route.";
                    var sb = new StringBuilder();
                    var location = osmIndex + 1;
                    for (int i =0 ; i < netexOffset; i++) {
                        netexQuay = netexQuays.get(netexIndex++);
                        quayMatches.add(new QuayMatch(null, netexQuay));
                        sb.append("\n").append(netexQuay.getName());
                    }
                    addIssue("MissingQuays",
                            Integer.toString(netexOffset), Integer.toString(location), quayBefore, quayAfter, sb.toString());
                    continue;
                }
                else if (netexOffset == -1 && osmOffset == -1) {
                    quayMatches.add(new QuayMatch(osmQuay, netexQuay));
                    addIssue("DifferentQuay", Integer.toString(osmIndex + 1), netexQuay.getName(), osmQuay.getName());
                    osmIndex++;
                    netexIndex++;
                    continue;
                }
                else if (osmOffset <= netexOffset) {
                    var sb = new StringBuilder();
                    var location = osmIndex + 1;
                    for (int i =0 ; i < osmOffset; i++) {
                        osmQuay = osmQuays.get(osmIndex++);
                        quayMatches.add(new QuayMatch(osmQuay, null));
                        sb.append("\n").append(osmQuay.getName());
                    }
                    addIssue("UnexpectedQuays", Integer.toString(osmOffset), Integer.toString(location), sb.toString());
                    continue;
                }
                else if (osmOffset > netexOffset) {
                    String quayBefore = (osmIndex > 0 && (osmIndex - 1) < osmQuays.size()) ? osmQuays.get(osmIndex - 1).getName() : "start of route";
                    String quayAfter = (osmIndex < osmQuays.size()) ? osmQuays.get(osmIndex).getName() : "end of route.";
                    var sb = new StringBuilder();
                    var location = osmIndex + 1;
                    for (int i =0 ; i < netexOffset; i++) {
                        netexQuay = netexQuays.get(netexIndex++);
                        quayMatches.add(new QuayMatch(null, netexQuay));
                        sb.append("\n").append(netexQuay.getName());
                    }
                    addIssue("MissingQuays",
                            Integer.toString(netexOffset), Integer.toString(location), quayBefore, quayAfter, sb.toString());
                }
                else {
                    osmIndex++;
                    netexIndex++;
                    continue;
               }
            }
            // Check for trailing Netex quays
            if (netexIndex < netexQuays.size()) {
                createMissingQuaysAtEndOfRouteIssue(netexQuays.size() - netexIndex);
            }
            // Check for trailing OSM quays
            if (osmIndex < osmQuays.size()) {
                createUnexpectedQuaysAtEndOfRouteIssue(osmQuays.size() - osmIndex);
            }
        }
    
        private int findOsmOffset(NetexRouteVariantQuay netexQuay) {
            var index = osmIndex + 1;
            while (index < osmQuays.size()) {
                var osmQuay = osmQuays.get(index);
                if (quaysMatch(osmQuay, netexQuay)) return index - osmIndex;
                index++;
            }
            return -1;
        }

        private Integer findNetexOffset(OsmRouteQuay osmQuay) {
            var index = netexIndex + 1;
            while (index < netexQuays.size()) {
                var netexQuay = netexQuays.get(index);
                if (quaysMatch(osmQuay, netexQuay)) return index - netexIndex;
                index++;
            }
            return -1;
        }

        private boolean quaysMatch(OsmRouteQuay osmQuay, NetexRouteVariantQuay netexQuay) {
            return Objects.equals(osmQuay.getQuayCode(), netexQuay.getQuayCode()) ||
                    Objects.equals(osmQuay.getAreaCode(), netexQuay.getStopPlaceCode());
        }
        
        private void analizeQuayMatch(QuayMatch match) {
            if (match.isAreaCodeMatch() && ! match.isQuayCodeMatch()) {
                String expected = match.getNetexQuay().getName();
                String found = match.getOsmQuay().getQuayCode();
                addIssue("UnexpectedQuayCode",
                        match.getOsmQuay().getName(), Integer.toString(osmIndex + 1), expected, found);
            }
        }

        private void createMissingQuaysAtEndOfRouteIssue(int count) {
            var sb = new StringBuilder();
            for (int i =0 ; i < count; i++) {
                var netexQuay = netexQuays.get(netexIndex++);
                quayMatches.add(new QuayMatch(null, netexQuay));
                sb.append("\n").append(netexQuay.getName());
            }
            addIssue("MissingQuaysAtEndOfRoute",
                  Integer.toString(count), sb.toString());
        }

        private void createUnexpectedQuaysAtEndOfRouteIssue(int count) {
            var sb = new StringBuilder();
            for (int i =0 ; i < count; i++) {
                var osmQuay = osmQuays.get(osmIndex++);
                quayMatches.add(new QuayMatch(osmQuay, null));
                sb.append("\n").append(osmQuay.getName());
            }
            addIssue("UnexpectedQuaysAtEndOfRoute", Integer.toString(count), sb.toString());
        }

        private RouteIssueData addIssue(String message, String... parameters) {
            var issue = new RouteIssueData(routeMatch.getId(), issues.size(),
                    message, parameters);
            issues.add(issue);
            return issue;
        }
    }
}
