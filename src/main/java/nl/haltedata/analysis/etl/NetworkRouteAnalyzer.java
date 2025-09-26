package nl.haltedata.analysis.etl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.NetworkMatch;
import nl.haltedata.analysis.dto.RouteIssueDataRepository;
import nl.haltedata.analysis.dto.RouteMatchRepository;

@Component
@Scope("prototype")
public class NetworkRouteAnalyzer {
    @Inject RouteMatchRepository routeMatchRepository;
    @Inject RouteIssueDataRepository routeIssueDataRepository;
    @Inject RouteAnalyzer routeAnalizer;
    
    @Transactional
    public void analize(NetworkMatch networkMatch) {
        routeIssueDataRepository.deleteByAdministrativeZone(networkMatch.getAdministrativeZone());
        var routeMatches = routeMatchRepository.findByAdministrativeZone(networkMatch.getAdministrativeZone());
        for (var routeMatch : routeMatches) {
            if (routeMatch.getNetexVariantId() != null && routeMatch.getMatchRate() > 0 && routeMatch.getMatchRate() < 100) {
                routeAnalizer.analize(routeMatch);
            }
        }
    }
 }
