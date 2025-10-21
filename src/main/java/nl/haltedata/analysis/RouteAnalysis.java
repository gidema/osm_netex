package nl.haltedata.analysis;

import java.util.List;

import nl.haltedata.analysis.dto.RouteIssueDataDto;
import nl.haltedata.analysis.dto.RouteMatchDto;
import nl.haltedata.osm.dto.OsmRouteDto;

public class RouteAnalysis {
    private final RouteMatchDto routeMatch;
    private final OsmRouteDto osmRoute;
    private final List<RouteIssueDataDto> matchIssues;
    private final int quayCountDifference;
    
    @SuppressWarnings("exports")
    public RouteAnalysis(RouteMatchDto routeMatch, OsmRouteDto osmRoute, List<RouteIssueDataDto> matchIssues, int quayCountDifference) {
        super();
        this.routeMatch = routeMatch;
        this.osmRoute = osmRoute;
        this.matchIssues = matchIssues;
        this.quayCountDifference = quayCountDifference;
    }

    @SuppressWarnings("exports")
    public RouteMatchDto getRouteMatch() {
        return routeMatch;
    }


    @SuppressWarnings("exports")
    public OsmRouteDto getOsmRoute() {
        return osmRoute;
    }


    @SuppressWarnings("exports")
    public List<RouteIssueDataDto> getMatchIssues() {
        return matchIssues;
    }

//    public int getIssueCount() {
//        return getMatchIssues().size();
//    }
//    
    /**
     * Get a score which is an indication of how similar the compared routes are.
     * Lower scores are better, with 0 indicating a perfect match
     * 
     * @return
     */
    public int getScore() {
        return quayCountDifference + getMatchIssues().size();
    }
}
