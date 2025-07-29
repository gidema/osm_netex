package nl.haltedata.analysis.etl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
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
import nl.haltedata.util.I18n;
import nl.haltedata.util.MockI18n;

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
 * If still no match is found, a mismatch is returned containing the first quays on both sides an the
 * indexes are incremented.
 */
@Component
public class RouteAnalizerFactory {
    @Inject
    OsmRouteQuayRepository osmRouteQuayRepository;
    
    @Inject
    NetexRouteVariantQuayRepository netexRouteVariantQuayRepository;

    @Inject
    RouteMatchRepository routeMatchRepository;

    @Inject
    OsmRouteRepository osmRouteRepository;
    
    @Inject
    NetexRouteVariantRepository netexRouteVariantRepository;
    
    @Inject
    RouteIssueDataRepository routeIssueDataRepository;
    
    public RouteAnalizer getRouteAnalizer(Locale locale) {
        return new RouteAnalizer(locale, osmRouteQuayRepository, netexRouteVariantQuayRepository, routeMatchRepository, osmRouteRepository, netexRouteVariantRepository, routeIssueDataRepository);
    }
}
