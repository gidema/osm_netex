package nl.haltedata.analysis;

import java.util.Comparator;
import java.util.Locale;

import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.NetworkMatch;
import nl.haltedata.analysis.dto.RouteIssueDataRepository;
import nl.haltedata.analysis.dto.RouteMatch;
import nl.haltedata.analysis.dto.RouteMatchRepository;
import nl.haltedata.util.ResourceBundleI18n;

@Component
public class HtmlNetworkRouteReporter implements NetworkRouteReporter {
    @Inject private RouteIssueDataRepository routeIssueDataRepository;
    @Inject private RouteMatchRepository routeMatchRepository;

    private Comparator<RouteMatch> lineSortcomparator = new RouteMatch.LineSortComparator();

    @Override
    public CharSequence getReport(NetworkMatch networkMatch, Locale locale) {
        var sb = new StringBuilder(10000);
        sb.append("<html>\n<body>\n");
        var routeMatches = routeMatchRepository.findByAdministrativeZone(networkMatch.getAdministrativeZone());
        routeMatches.sort(lineSortcomparator);
        for (var routeMatch : routeMatches) {
            if (routeMatch.getMatchRate() > 0 && routeMatch.getMatchRate() < 100) {
                sb.append(report(routeMatch, locale));
            }
        }
        sb.append("</body>\n</html>");
        return sb;
    }

    private CharSequence report(RouteMatch routeMatch, Locale locale) {
        var i18n = new ResourceBundleI18n(locale);
        var matchIssues = routeIssueDataRepository.findByRouteMatchId(routeMatch.getId());
        var sb = new StringBuilder();
        sb.append("<h2>").append(i18n.tr("CheckingOSMRoute",
            routeMatch.getOsmRouteId().toString(),
            routeMatch.getOsmName()))
            .append("</h2>\n");
        if (matchIssues.size() == 0) {
            sb.append(i18n.tr("No issues were found for this route"));
        }
        else {
            sb.append("<p>");
            matchIssues.forEach(issue -> {
                var msg = i18n.tr(issue.getMessage(), issue.getParameters());
                sb.append(msg.replace("\n", "</br>")).append("\n</br>");
            });
            sb.append("<p>\n");
        }
        return sb;
    }
}
