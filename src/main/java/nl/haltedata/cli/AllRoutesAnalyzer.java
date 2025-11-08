package nl.haltedata.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.RouteIssueDataRepository;
import nl.haltedata.analysis.etl.RouteAnalyzer;
import nl.haltedata.analysis.services.LineMatchService;
import nl.haltedata.analysis.services.NetworkMatchService;
import nl.haltedata.analysis.services.RouteMatchService;

@Component
public class AllRoutesAnalyzer implements ApplicationRunner {
    
    private static Logger LOG = LoggerFactory
            .getLogger(AllRoutesAnalyzer.class);

    @Inject NetworkMatchService networkMatchService;
    @Inject LineMatchService lineMatchService;
    @Inject RouteMatchService routeMatchService;
    @Inject RouteIssueDataRepository routeIssueDataRepository;
    @Inject RouteAnalyzer routeAnalizer;

    @SuppressWarnings("exports")
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("EXECUTING : All routes analyzer");
        routeIssueDataRepository.deleteAll();
        var routeMatches = routeMatchService.findAll(false);
        routeMatches.forEach(rm -> {
            routeAnalizer.analize(rm);
        });
        routeMatchService.updateIssueStatistics();
        lineMatchService.updateIssueStatistics();
        networkMatchService.updateIssueStatistics();
    }
}
