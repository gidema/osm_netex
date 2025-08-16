package nl.haltedata.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Import(value = {nl.haltedata.cli.RouteAnalyzerCommand.Config.class, AllRoutesAnalyzer.class})
@EnableJpaRepositories(basePackages = {"nl.haltedata.analysis.dto", "nl.haltedata.osm.dto", "nl.haltedata.netex.dto"})
@ComponentScan(useDefaultFilters = false)
//    excludeFilters = { @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.gtfs.config.batch.*")}
//)useDefaultFilters
@EntityScan({"nl.haltedata.analysis.dto", "nl.haltedata.netex.dto", "nl.haltedata.osm.dto"})
public class RouteAnalyzerCommand {

    private static Logger LOG = LoggerFactory.getLogger(RouteAnalyzerCommand.class);
    
    public RouteAnalyzerCommand() {
        
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        new SpringApplicationBuilder(RouteAnalyzerCommand.class)
            .web(WebApplicationType.NONE)
            .run(args);
        LOG.info("APPLICATION FINISHED");
    }
    
    @Configuration
    @ComponentScan(basePackages = {"nl.haltedata.analysis", "nl.haltedata.analysis.etl"})
    static class Config {
        //
    }
}
