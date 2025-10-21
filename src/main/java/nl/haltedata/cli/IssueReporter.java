package nl.haltedata.cli;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.haltedata.analysis.NetworkRouteReporter;
import nl.haltedata.analysis.dto.NetworkMatchDto;
import nl.haltedata.analysis.etl.NetworkRouteAnalyzerFactory;
import nl.haltedata.analysis.services.NetworkMatchService;

@Component
public class IssueReporter implements ApplicationRunner, InitializingBean {

    @Inject NetworkMatchService networkService;
    @Inject NetworkRouteAnalyzerFactory analizerFactory;
    @Inject NetworkRouteReporter reporter;
    
    @Value("${osm_netex.path.results}")
    private String resultPathProperty;
    
    private Path reportPath;

    private static Logger LOG = LoggerFactory
            .getLogger(IssueReporter.class);

    @Override
    public void afterPropertiesSet() {
        reportPath = Path.of(resultPathProperty, "analysis");
        reportPath.toFile().mkdirs();
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("EXECUTING : All routes analyzer");
        networkService.findAllByOrderByName().forEach(networkMatch -> {
            if (networkMatch.getAdministrativeZone() != null) {
                reportNetworkMatch(networkMatch);
            }
        });
    }
    
    private void reportNetworkMatch(NetworkMatchDto networkMatch) {
        LOG.info("Reporting {}", networkMatch.getAdministrativeZone());
        reportAdministrativeZone(networkMatch, new Locale("nl"));
    }
    
    private void reportAdministrativeZone(NetworkMatchDto networkMatch, Locale locale) {
        var filePath = reportPath.resolve(networkMatch.getName().replace('/', '_').concat(".html"));
        try (
                var writer = new FileWriter(filePath.toString());
        ) {
            writer.write(reporter.getReport(networkMatch, locale).toString());
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
