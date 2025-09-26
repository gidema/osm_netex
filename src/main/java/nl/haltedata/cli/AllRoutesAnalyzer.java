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
import nl.haltedata.analysis.dto.NetworkMatch;
import nl.haltedata.analysis.dto.NetworkMatchRepository;
import nl.haltedata.analysis.etl.NetworkRouteAnalyzerFactory;

@Component
public class AllRoutesAnalyzer implements ApplicationRunner, InitializingBean {

    @Inject NetworkMatchRepository networkRepository;
    @Inject NetworkRouteAnalyzerFactory analizerFactory;
    @Inject NetworkRouteReporter reporter;
    
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
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("EXECUTING : All routes analyzer");
        networkRepository.findAll().forEach(networkMatch -> {
            if (networkMatch.getAdministrativeZone() != null) {
                analizeNetworkMatch(networkMatch);
            }
        });
    }
    
    private void analizeNetworkMatch(NetworkMatch networkMatch) {
        LOG.info("Analizing {}", networkMatch.getAdministrativeZone());
        var analizer = analizerFactory.getAnalyzer();
        analizer.analize(networkMatch);
        reportAdministrativeZone(networkMatch, new Locale("nl"));
    }
    
    private void reportAdministrativeZone(NetworkMatch networkMatch, Locale locale) {
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
