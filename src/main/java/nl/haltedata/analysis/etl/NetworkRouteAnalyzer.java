package nl.haltedata.analysis.etl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.NetworkMatchDto;
import nl.haltedata.analysis.dto.RouteIssueDataRepository;
import nl.haltedata.analysis.services.RouteMatchService;

@Component
@Scope("prototype")
public class NetworkRouteAnalyzer {
    @Inject RouteMatchService routeMatchService;
    @Inject RouteIssueDataRepository routeIssueDataRepository;
    @Inject RouteAnalyzer routeAnalizer;
    
    @SuppressWarnings("exports")
    @Transactional
    public void analize(NetworkMatchDto networkMatch) {
        networkMatch.getLineMatches().forEach(lineMatch -> {
            lineMatch.getRouteMatches().forEach(routeMatch -> {
                if (routeMatch.getNetexVariant() != null && routeMatch.getMatchRate() > 0 && routeMatch.getMatchRate() < 100) {
                    routeAnalizer.analize(routeMatch);
                }
            });
        });
    }
 }
