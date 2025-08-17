package nl.haltedata.cli;

import java.io.File;
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
import nl.haltedata.analysis.etl.NetworkRouteAnalyzerFactory;
import nl.haltedata.osm.dto.OsmPtNetworkRepository;

@Component
public class AllRoutesAnalyzer implements ApplicationRunner, InitializingBean {

    @Inject OsmPtNetworkRepository networkRepository;
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
        networkRepository.findAllByCountryCode("NL").forEach(network -> {
            analizeNetwork(network.getNetworkName());
        });
    }
    
    private void analizeNetwork(String network) {
        LOG.info("Analizing {}", network);
        var analizer = analizerFactory.getAnalyzer();
        analizer.analize(network);
        reportNetwork(network, new Locale("nl"));
    }
    
    private void reportNetwork(String network, Locale locale) {
        var filePath = reportPath.resolve(network.replace('/', '_').concat(".html"));
        try (
                var writer = new FileWriter(filePath.toString());
        ) {
            writer.write(reporter.getReport(network, locale).toString());
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
