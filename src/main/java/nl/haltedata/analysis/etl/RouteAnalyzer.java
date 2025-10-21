package nl.haltedata.analysis.etl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.haltedata.analysis.RouteAnalysis;
import nl.haltedata.analysis.dto.RouteIssueDataDto;
import nl.haltedata.analysis.dto.RouteMatchDto;
import nl.haltedata.analysis.services.RouteIssueDataService;
import nl.haltedata.analysis.services.RouteMatchService;
import nl.haltedata.netex.dto.NetexRouteVariantDto;
import nl.haltedata.netex.dto.NetexRouteVariantQuayDto;
import nl.haltedata.osm.dto.OsmRouteDto;
import nl.haltedata.osm.dto.OsmRouteQuayDto;
import nl.haltedata.osm.dto.OsmRouteQuayRepository;

/**
 * The RouteAnalyzer iterates over a list of NeTeX quays and a list of OSM quay and tries to find
 * the next QuayMatch.
 * If the first quays on both sides match, a QuayMatch is returned containing the matching quays.
 *  The match is tried on quayCode, stopPlace and name in that order. If there is a match on stopPlace,
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
    private static Comparator<OsmRouteQuayDto> osmQuayComparator = new Comparator<>() {
        @Override
        public int compare(OsmRouteQuayDto o1, OsmRouteQuayDto o2) {
            return Long.compare(o1.getQuayIndex(), o2.getQuayIndex());
        }
    };
    
    private static Comparator<NetexRouteVariantQuayDto> netexQuayComparator = new Comparator<>() {
        @Override
        public int compare(NetexRouteVariantQuayDto o1, NetexRouteVariantQuayDto o2) {
            return Long.compare(o1.getQuayIndex(), o2.getQuayIndex());
        }
    };
    @Inject
    OsmRouteQuayRepository osmRouteQuayRepository;
    
    @Inject
    private RouteMatchService routeMatchService;
    
    @Inject
    private RouteIssueDataService routeIssueDataService;
    
    public RouteAnalysis analize(Long routeMatchId) {
        var rm = routeMatchService.findById(routeMatchId).get();
        return analize(rm);
    }

    public RouteAnalysis analize(@SuppressWarnings("exports") RouteMatchDto match) {
        var analizer = new Analizer();
        return analizer.analize(match);
    }
    
    class Analizer {
        private RouteMatchDto routeMatch;
        private OsmRouteDto osmRoute;
        private NetexRouteVariantDto netexRouteVariant;
        private List<OsmRouteQuayDto> osmQuays;
        private List<NetexRouteVariantQuayDto> netexQuays;
        private List<QuayMatch> quayMatches = new LinkedList<>();
        private final List<RouteIssueDataDto> issues = new LinkedList<>();
        int osmIndex = 0;
        int netexIndex = 0;

        public RouteAnalysis analize(RouteMatchDto match) {
            this.routeMatch = match;
            osmRoute = match.getOsmRoute();
            netexRouteVariant = match.getNetexVariant();
            checkRouteHeader();
            osmQuays = new ArrayList<>(osmRoute.getQuays());
            osmQuays.sort(osmQuayComparator);
            netexQuays = new ArrayList<>(netexRouteVariant.getQuays());
            netexQuays.sort(netexQuayComparator);
            checkRouteQuays();
            int quayCountDifference = Math.abs(netexQuays.size() - osmQuays.size());
            routeIssueDataService.saveAll(issues);
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
                    addIssue("MissingRouteColour", new String[0], "#" + netexColour);
                }
            }
            else if (osmColour != null && netexColour == null) {
                addIssue("UnexpectedRouteColour1", new String[0], osmColour);
            }
            else if (!ColourMap.normalizeColour(osmColour).equals("#" + netexColour)) {
                addIssue("UnexpectedRouteColour2", new String[0], "#" + netexColour, osmColour);
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
                    addIssue("DifferentQuay", new String[0], Integer.toString(osmIndex + 1), netexQuay.getName(), osmQuay.getName());
                    osmIndex++;
                    netexIndex++;
                }
                // Extra quays in the OSM route
                else if (netexOffset == -1 && osmOffset > 0) {
                    var lines = new String[osmOffset];
                    var location = osmIndex + 1;
                    for (int i =0 ; i < osmOffset; i++) {
                        osmQuay = osmQuays.get(osmIndex++);
                        quayMatches.add(new QuayMatch(osmQuay, null));
                        lines[i] = osmQuay.getName();
                    }
                    addIssue("UnexpectedQuays", lines, Integer.toString(osmOffset), Integer.toString(location));
                    continue;
                }
                // Missing quays in the OSM route
                else if (osmOffset == -1 && netexOffset > 0) {
                    String quayBefore = (osmIndex > 0 && (osmIndex - 1) < osmQuays.size()) ? osmQuays.get(osmIndex - 1).getName() : "start of route";
                    String quayAfter = (osmIndex < osmQuays.size()) ? osmQuays.get(osmIndex).getName() : "end of route.";
                    var lines = new String[netexOffset];
                    var location = osmIndex + 1;
                    for (int i =0 ; i < netexOffset; i++) {
                        netexQuay = netexQuays.get(netexIndex++);
                        quayMatches.add(new QuayMatch(null, netexQuay));
                        lines[i] =netexQuay.getName();
                    }
                    addIssue("MissingQuays", lines,
                            Integer.toString(netexOffset), Integer.toString(location), quayBefore, quayAfter);
                    continue;
                }
                else if (netexOffset == -1 && osmOffset == -1) {
                    quayMatches.add(new QuayMatch(osmQuay, netexQuay));
                    addIssue("DifferentQuay", new String[0], Integer.toString(osmIndex + 1), netexQuay.getName(), osmQuay.getName());
                    osmIndex++;
                    netexIndex++;
                    continue;
                }
                else if (osmOffset <= netexOffset) {
                    var lines = new String[osmOffset];
                    var location = osmIndex + 1;
                    for (int i =0 ; i < osmOffset; i++) {
                        osmQuay = osmQuays.get(osmIndex++);
                        quayMatches.add(new QuayMatch(osmQuay, null));
                        lines[i] = osmQuay.getName();
                    }
                    addIssue("UnexpectedQuays", lines, Integer.toString(osmOffset), Integer.toString(location));
                    continue;
                }
                else if (osmOffset > netexOffset) {
                    String quayBefore = (osmIndex > 0 && (osmIndex - 1) < osmQuays.size()) ? osmQuays.get(osmIndex - 1).getName() : "start of route";
                    String quayAfter = (osmIndex < osmQuays.size()) ? osmQuays.get(osmIndex).getName() : "end of route.";
                    var lines = new String[netexOffset];
                    var location = osmIndex + 1;
                    for (int i =0 ; i < netexOffset; i++) {
                        netexQuay = netexQuays.get(netexIndex++);
                        quayMatches.add(new QuayMatch(null, netexQuay));
                        lines[i] = netexQuay.getName();
                    }
                    addIssue("MissingQuays", lines,
                            Integer.toString(netexOffset), Integer.toString(location), quayBefore, quayAfter);
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
    
        private int findOsmOffset(NetexRouteVariantQuayDto netexQuay) {
            var index = osmIndex + 1;
            while (index < osmQuays.size()) {
                var osmQuay = osmQuays.get(index);
                if (quaysMatch(osmQuay, netexQuay)) return index - osmIndex;
                index++;
            }
            return -1;
        }

        private Integer findNetexOffset(OsmRouteQuayDto osmQuay) {
            var index = netexIndex + 1;
            while (index < netexQuays.size()) {
                var netexQuay = netexQuays.get(index);
                if (quaysMatch(osmQuay, netexQuay)) return index - netexIndex;
                index++;
            }
            return -1;
        }

        private boolean quaysMatch(OsmRouteQuayDto osmQuay, NetexRouteVariantQuayDto netexQuay) {
            return Objects.equals(osmQuay.getQuayCode(), netexQuay.getQuayCode()) ||
                    Objects.equals(osmQuay.getStopPlace(), netexQuay.getStopPlaceCode());
        }
        
        private void analizeQuayMatch(QuayMatch match) {
            if (match.isStopPlaceMatch() && ! match.isQuayCodeMatch()) {
                String expected = match.getNetexQuay().getName();
                String found = match.getOsmQuay().getQuayCode();
                addIssue("UnexpectedQuayCode", new String[0],
                        match.getOsmQuay().getName(), Integer.toString(osmIndex + 1), expected, found);
            }
        }

        private void createMissingQuaysAtEndOfRouteIssue(int count) {
            var lines = new String[count];
            for (int i =0 ; i < count; i++) {
                var netexQuay = netexQuays.get(netexIndex++);
                quayMatches.add(new QuayMatch(null, netexQuay));
                lines[i] = netexQuay.getName();
            }
            addIssue("MissingQuaysAtEndOfRoute", lines,
                  Integer.toString(count));
        }

        private void createUnexpectedQuaysAtEndOfRouteIssue(int count) {
            var lines = new String[count];
            for (int i =0 ; i < count; i++) {
                var osmQuay = osmQuays.get(osmIndex++);
                quayMatches.add(new QuayMatch(osmQuay, null));
                lines[i] = osmQuay.getName();
            }
            addIssue("UnexpectedQuaysAtEndOfRoute", lines, Integer.toString(count));
        }

        private RouteIssueDataDto addIssue(String message, String[] lines, String... parameters) {
            var issue = new RouteIssueDataDto(routeMatch, issues.size(),
                    message, parameters, lines);
            issues.add(issue);
            return issue;
        }
    }
}
