package nl.haltedata.cli;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Import(value= {nl.haltedata.cli.AnalysisUpdateCommand.Config.class, AnalysisUpdater.class})
@ComponentScan(useDefaultFilters = false)
@EntityScan(basePackages = {"nl.haltedata.analysis.dto", "nl.haltedata.osm.dto", "nl.haltedata.netex.dto", "nl.haltedata.chb.dto"})
@EnableJpaRepositories(basePackages = {"nl.haltedata.analysis.dto", "nl.haltedata.osm.dto","nl.haltedata.netex.dto"})
public class AnalysisUpdateCommand {

    private static Logger LOG = LoggerFactory.getLogger(AnalysisUpdateCommand.class);

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        new SpringApplicationBuilder(AnalysisUpdateCommand.class)
            .web(WebApplicationType.NONE)
            .run(args);
        LOG.info("APPLICATION FINISHED");
    }
    
    @Configuration
    @ComponentScan(basePackages = {"nl.haltedata.analysis", "nl.haltedata.analysis.etl"})
    static class Config {
        
        @Bean
        static
        ModelMapper getModelMapper() {
            var modelMapper = new ModelMapper();
            return modelMapper;
        }
    }
}