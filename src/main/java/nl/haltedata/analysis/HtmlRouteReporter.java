package nl.haltedata.analysis;

import java.util.Locale;

import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.RouteIssueDataRepository;
import nl.haltedata.analysis.dto.RouteMatch;
import nl.haltedata.util.ResourceBundleI18n;

@Component
public class HtmlRouteReporter {
    @Inject
    private RouteIssueDataRepository routeIssueDataRepository;
    
    @SuppressWarnings("exports")
    public CharSequence report(RouteMatch routeMatch, Locale locale) {
        var i18n = new ResourceBundleI18n(locale);
        var matchIssues = routeIssueDataRepository.findByRouteMatchId(routeMatch.getId());
        var sb = new StringBuilder();
        sb.append("<h2>").append(i18n.tr("CheckingOSMRoute",
            routeMatch.getOsmRouteId().toString(),
            routeMatch.getOsmName()))
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
        return sb;
    }
}
