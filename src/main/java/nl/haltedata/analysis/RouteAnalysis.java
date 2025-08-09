package nl.haltedata.analysis;

import java.util.List;

import nl.haltedata.analysis.dto.RouteIssueData;
import nl.haltedata.analysis.dto.RouteMatch;
import nl.haltedata.osm.dto.OsmRoute;

public class RouteAnalysis {
    private final RouteMatch routeMatch;
    private final OsmRoute osmRoute;
    private final List<RouteIssueData> matchIssues;
    private final int quayCountDifference;
    
    @SuppressWarnings("exports")
    public RouteAnalysis(RouteMatch routeMatch, OsmRoute osmRoute, List<RouteIssueData> matchIssues, int quayCountDifference) {
        super();
        this.routeMatch = routeMatch;
        this.osmRoute = osmRoute;
        this.matchIssues = matchIssues;
        this.quayCountDifference = quayCountDifference;
    }

    @SuppressWarnings("exports")
    public RouteMatch getRouteMatch() {
        return routeMatch;
    }


    @SuppressWarnings("exports")
    public OsmRoute getOsmRoute() {
        return osmRoute;
    }


    @SuppressWarnings("exports")
    public List<RouteIssueData> getMatchIssues() {
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
