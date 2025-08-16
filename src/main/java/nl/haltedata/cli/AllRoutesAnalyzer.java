package nl.haltedata.cli;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.haltedata.analysis.NetworkRouteReporter;
import nl.haltedata.analysis.etl.NetworkRouteAnalyzerFactory;
import nl.haltedata.osm.dto.OsmPtNetworkRepository;

@Component
public class AllRoutesAnalyzer implements ApplicationRunner {

    @Inject OsmPtNetworkRepository networkRepository;
    @Inject NetworkRouteAnalyzerFactory analizerFactory;
    @Inject NetworkRouteReporter reporter;
    
    private static Logger LOG = LoggerFactory
            .getLogger(AllRoutesAnalyzer.class);

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
        try (
                var writer = new FileWriter(String.format("/home/gertjan/Apps/osm_netex/results/analysis/%s.html", 
                    network.replace('/', '_')));
        ) {
            writer.write(reporter.getReport(network, locale).toString());
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
