package nl.haltedata;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.inject.Inject;
import nl.haltedata.analysis.etl.NetworkRouteAnalizer;
import nl.haltedata.osm.dto.OsmPtNetworkRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "nl.haltedata.osm.dto", "nl.haltedata.chb.dto", "nl.haltedata.analysis.dto", "nl.haltedata.netex.dto",
"nl.haltedata.validation.model", "nl.haltedata.analysis"})
@ComponentScan(basePackages = { "nl.haltedata.netex", "nl.haltedata.chb", "nl.haltedata.analysis", "nl.haltedata.analysis.dto", "nl.haltedata.osm", "nl.haltedata.validation"},
excludeFilters = { @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.gtfs.config.batch.*"),
    @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.netex.config.batch.*"),
    @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.chb.config.batch.*"),
    @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.osm.config.batch.*")})
@EntityScan({"nl.haltedata.analysis.dto", "nl.haltedata.netex.dto", "nl.haltedata.osm.dto", "nl.haltedata.validation.model"})
public class RouteAnalizerTest implements CommandLineRunner {

    @Inject
    OsmPtNetworkRepository networkRepository;
    
    private static Logger LOG = LoggerFactory
            .getLogger(RouteAnalizerTest.class);

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        new SpringApplicationBuilder(RouteAnalizerTest.class)
            .web(WebApplicationType.NONE)
            .run(args);
        LOG.info("APPLICATION FINISHED");
    }
    
    @SuppressWarnings("static-method")
    @Bean
    public NetworkRouteAnalizer analizer() {
        return new NetworkRouteAnalizer();
    }
    
    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");
        networkRepository.findAllByCountryCode("NL").forEach(network -> {
            analizeNetwork(network.getNetworkName());
        });
    }
    
    private void analizeNetwork(String network) {
        var analizer = analizer();
        analizer.analize(network);
        var report = analizer.getReport();
        try (
            var writer = new FileWriter(String.format("/home/gertjan/Apps/osm_netex/results/analysis/%s.html", network));
        ) {
            writer.write(report);
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
