package nl.haltedata.cli;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.NetworkMatchDto;
import nl.haltedata.analysis.dto.RouteIssueDataRepository;
import nl.haltedata.analysis.etl.NetworkRouteAnalyzerFactory;
import nl.haltedata.analysis.etl.RouteAnalyzer;
import nl.haltedata.analysis.services.NetworkMatchService;
import nl.haltedata.analysis.services.RouteMatchService;

@Component
public class AllRoutesAnalyzer implements ApplicationRunner, InitializingBean {

    @Inject NetworkMatchService networkService;
    @Inject RouteMatchService routeMatchService;
    @Inject NetworkRouteAnalyzerFactory analizerFactory;
    @Inject RouteIssueDataRepository routeIssueDataRepository;
    @Inject RouteAnalyzer routeAnalizer;
    
    @Value("${osm_netex.path.results}")
    private String resultPathProperty;
    
    private Path reportPath;

    private static Logger LOG = LoggerFactory
            .getLogger(AllRoutesAnalyzer.class);

    @Override
    public void afterPropertiesSet() {
        reportPath = Path.of(resultPathProperty, "analysis");
        reportPath.toFile().mkdirs();
    }
    
    @SuppressWarnings("exports")
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("EXECUTING : All routes analyzer");
        routeIssueDataRepository.deleteAll();
        var routeMatches = routeMatchService.findAll();
        routeMatches.forEach(rm -> {
            routeAnalizer.analize(rm);
        });
//        networkService.findAllByOrderByName().forEach(networkMatch -> {
//            if (networkMatch.getAdministrativeZone() != null) {
//                analizeNetworkMatch(networkMatch);
//            }
//        });
    }
    
    private void analizeNetworkMatch(NetworkMatchDto networkMatch) {
        LOG.info("Analizing {}", networkMatch.getAdministrativeZone());
        var analizer = analizerFactory.getAnalyzer();
        analizer.analize(networkMatch);
    }
}
