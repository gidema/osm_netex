package nl.haltedata.analysis;

import java.util.List;
import java.util.Locale;

import nl.haltedata.analysis.dto.RouteIssueData;
import nl.haltedata.analysis.dto.RouteMatch;
import nl.haltedata.osm.dto.OsmRoute;
import nl.haltedata.util.I18n;
import nl.haltedata.util.ResourceBundleI18n;

public class RouteAnalysis {
    private final OsmRoute osmRoute;
    private final List<RouteIssueData> matchIssues;
    private final int quayCountDifference;
    private final I18n i18n; 
    
    @SuppressWarnings("exports")
    public RouteAnalysis(Locale locale, RouteMatch routeMatch, OsmRoute osmRoute, List<RouteIssueData> matchIssues, int quayCountDifference) {
        super();
        this.matchIssues = matchIssues;
        this.osmRoute = osmRoute;
        this.quayCountDifference = quayCountDifference;
        this.i18n = new ResourceBundleI18n(locale);
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
    public String getIssueReport() {
        var sb = new StringBuilder();
        sb.append("<h2>").append(i18n.tr("CheckingOSMRoute",
            osmRoute.getOsmRouteId().toString(),
            osmRoute.getName(), 
            "?"))
            .append("</h2>\n");
//            osmRoute.getRef()));
        if (matchIssues.size() == 0) {
            sb.append(i18n.tr("No issues were found for this route"));
        }
        else {
            sb.append("<p>");
            matchIssues.forEach(issue -> sb.append(i18n.tr(issue.getMessage(), issue.getParameters())).append("\n</br>"));
            sb.append("<p>\n");
        }
        return sb.toString();
    }


}
