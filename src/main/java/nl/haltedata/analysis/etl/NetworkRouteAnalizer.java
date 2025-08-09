package nl.haltedata.analysis.etl;

import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.haltedata.analysis.HtmlRouteReporter;
import nl.haltedata.analysis.dto.RouteMatch;
import nl.haltedata.analysis.dto.RouteMatchRepository;

@Component
public class NetworkRouteAnalizer {
    private String report;
    private LineSortComparator lineSortcomparator = new LineSortComparator();
    private Locale locale = new Locale("nl");
    
    @Inject ApplicationContext ctx;
    @Inject RouteMatchRepository routeMatchRepository;
    @Inject HtmlRouteReporter reporter;
    @Inject RouteAnalizer routeAnalizer;
    
    public void analize(String network) {
        var routeMatches = routeMatchRepository.findByNetwork(network);
        routeMatches.sort(lineSortcomparator);
        var sb = new StringBuilder(10000);
        sb.append("<html>\n<body>\n");
        for (var routeMatch : routeMatches) {
            if (routeMatch.getMatchRate() > 0 && routeMatch.getMatchRate() < 100) {
                routeAnalizer.analize(routeMatch);
                sb.append(reporter.report(routeMatch, locale));
            }
        }
        sb.append("</body>\n</html>");
        report = sb.toString();
    }
    
    public String getReport() {
        return report;
    }

    static class LineSortComparator implements Comparator<RouteMatch> {
        @Override
        public int compare(RouteMatch rm1, RouteMatch rm2) {
            var lineSort1 = Objects.requireNonNullElse(rm1.getLineSort(), "");
            var lineSort2 = Objects.requireNonNullElse(rm2.getLineSort(), "");
            return lineSort1.compareTo(lineSort2);
        }
    }
    
//    @SuppressWarnings("static-method")
//    @Bean
//    @Scope("prototype")
//    RouteAnalizer routeAnalizer() {
//        return new RouteAnalizer();
//    }
 }
