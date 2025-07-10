package nl.haltedata.validation;

import java.util.List;

import nl.haltedata.netex.dto.DimNetexRoute;
import nl.haltedata.netex.dto.NetexRouteVariant;
import nl.haltedata.osm.dto.OsmPtRoute;
import nl.haltedata.osm.dto.OsmRoute;

public class RouteCheckResults {
    private final OsmRoute osmRoute;
    private final NetexRouteVariant netexQuaySequence;
    private final List<RouteIssue> matchIssues;
    private int quayCountDifference;
    private static I18n i18n = new MockI18n(); 
    
    public RouteCheckResults(OsmRoute osmRoute2, NetexRouteVariant netexQuaySequence, List<RouteIssue> matchIssues, int quayCountDifference) {
        super();
        this.osmRoute = osmRoute2;
        this.netexQuaySequence = netexQuaySequence;
        this.matchIssues = matchIssues;
        this.quayCountDifference = quayCountDifference;
    }

    public OsmRoute getOsmRoute() {
        return osmRoute;
    }

    public NetexRouteVariant getNetexQuaySequence() {
        return netexQuaySequence;
    }

    public List<RouteIssue> getMatchIssues() {
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
    public String getIssueReport() {
        var sb = new StringBuilder();
        sb.append("<span style=\"font-family:arial;font-size:12px;line-height:150%\"><b>");
        sb.append(i18n.tr("Checking OSM route {0} {1} (line {2}).\n</br>\n</br>", 
            osmRoute.getOsmRouteId(),
            osmRoute.getName(), 
            "?"));
//            osmRoute.getRef()));
        if (matchIssues.size() == 0) {
            sb.append(i18n.tr("No issues were found for this route"));
        }
        else {
            matchIssues.forEach(issue -> sb.append(issue.getMessage(i18n)).append("\n</br>"));
        }
        sb.append("<b/><span/>");
        return sb.toString();
    }


}
