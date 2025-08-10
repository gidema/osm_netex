package nl.haltedata.analysis.etl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.haltedata.analysis.QuayMatch;
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
 * The RouteAnalizer iterates over a list of NeTeX quays and a list of OSM quay and tries to find
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
public class RouteAnalizer {
//    private Locale locale;
    
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
        var routeMatch = routeMatchRepository.findById(routeMatchId).get();
        return analize(routeMatch);
    }
    
    public RouteAnalysis analize(RouteMatch match) {
        var analizer = new Analizer();
        return analizer.analize(match);
    }
    
    class Analizer {
        private RouteMatch routeMatch;
        private OsmRoute osmRoute;
        private NetexRouteVariant netexRouteVariant;
        private List<OsmRouteQuay> osmQuays = Collections.emptyList();
        private List<NetexRouteVariantQuay> netexQuays = Collections.emptyList();
        private final List<RouteIssueData> issues = new LinkedList<>();
        int osmIndex = 0;
        int netexIndex = 0;

        RouteAnalysis analize(RouteMatch match) {
            this.routeMatch = match;
            osmRoute = osmRouteRepository.findById(routeMatch.getOsmRouteId()).get();
            netexRouteVariant = netexRouteVariantRepository.findById(routeMatch.getNetexVariantId()).get();
            // TODO Auto-generated method stub
        
            osmQuays = osmRouteQuayRepository.findByOsmRouteId(osmRoute.getOsmRouteId());
            netexQuays = netexRouteVariantQuayRepository.
                findByVariantId(netexRouteVariant.getId());
            checkRouteHeader();
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
    //        Set<String> uniqueQuayCodes = osmRoute.getQuays().stream().map(quay -> quay.getQuayCode())
    //                .filter(code -> Objects.nonNull(code)).collect(Collectors.toSet());
    //        chbQuayService.fetchMissingCodes(uniqueQuayCodes);
            while (osmIndex < osmQuays.size() && netexIndex < netexQuays.size()) {
                nextMatch();
            }
            // Check for trailing Netex quays
            if (netexIndex < netexQuays.size()) {
                createMissingQuaysIssue(netexQuays.size() - netexIndex);
            }
            // Check for trailing OSM quays
            if (osmIndex < osmQuays.size()) {
                createExtraQuaysIssue(osmQuays.size() - osmIndex);
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
                    createExtraQuaysIssue(offset);
                    checkMatchIssues(osmOffsetMatch);
                    osmIndex = osmIndex + offset + 1;
                    netexIndex++;
                    return osmOffsetMatch;
                }
                if (netexOffsetMatch != null && netexOffsetMatch.isMatch()) {
                    createMissingQuaysIssue(offset);
                    checkMatchIssues(netexOffsetMatch);
                    netexIndex = netexIndex + offset + 1;
                    osmIndex++;
                    return netexOffsetMatch;
                }
            }
            // No match found, not even with offsetting. Report a mismatch issue and return the
            // mismatch;
            addIssue("DifferentQuay",
                    Integer.toString(osmIndex + 1), netexQuay.getName(), osmQuay.getName());
            osmIndex++;
            netexIndex++;
            return match;
        }
        
        private void createExtraQuaysIssue(int offset) {
            if (offset == 1) {
                var extraQuay = osmQuays.get(osmIndex);
                addIssue("UnexpectedQuay",
                    extraQuay.getName(), Integer.toString(osmIndex + 1));
                return;
            }
            for (int i = 0 ; i<offset; i++) {
                var extraQuay = osmQuays.get(osmIndex + i);
                addIssue("UnexpectedQuay",
                        extraQuay.getName(), Integer.toString(osmIndex + 1));
            }
        }
    
        private void createMissingQuaysIssue(int offset) {
            if (offset == 1) {
                var extraQuay = netexQuays.get(netexIndex);
                String quayBefore = (osmIndex > 0) ? osmQuays.get(osmIndex - 1).getName() : "start of route";
                String quayAfter = (osmIndex < osmQuays.size()) ? osmQuays.get(osmIndex).getName() : "end of route.";
                addIssue("MissingQuay",
                    extraQuay.getName(), Integer.toString(osmIndex + 1), quayBefore, quayAfter);
            }
            else {
                String quayBefore = (osmIndex > 0) ? osmQuays.get(osmIndex - 1).getName() : "start of route";
                int endIndex = osmIndex + offset;
                String quayAfter = (endIndex < osmQuays.size()) ? osmQuays.get(endIndex).getName() : "end of route.";
    //            sb.append(i18n.tr("Missing quays found in OSM route between {0} and {1}.:\n", quayBefore, quayAfter));
                for (int i = 0 ; i<offset; i++) {
                    var extraQuay = netexQuays.get(netexIndex + i);
                    var position = osmIndex + i + 1;
                    addIssue("MissingMultipleQuay",
                            extraQuay.getName(), quayBefore, Integer.toString(position++));
                }
            }
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
                String expected = match.getNetexQuay().getName();
                String found = match.getOsmQuay().getQuayCode();
                addIssue("UnexpectedQuayCode",
                    match.getOsmQuay().getName(), Integer.toString(osmIndex + 1), expected, found);
            }
        }
        
        private RouteIssueData addIssue(String message, String... parameters) {
            var issue = new RouteIssueData(routeMatch.getId(), issues.size(),
                    message, parameters);
            issues.add(issue);
            return issue;
        }
    }
}
