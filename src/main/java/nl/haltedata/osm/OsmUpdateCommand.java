package nl.haltedata.osm;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = { "nl.haltedata.osm" },
excludeFilters = { @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.osm.config.batch.*")})
public class OsmUpdateCommand implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
      .getLogger(OsmUpdateCommand.class);

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        new SpringApplicationBuilder(OsmUpdateCommand.class)
            .web(WebApplicationType.NONE)
            .run(args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) throws Exception {
        updater().run();
    }
    
    @SuppressWarnings("static-method")
    @Bean
    OsmUpdater updater() {
        return new OsmUpdater();
    }
    
    @Bean
    static ModelMapper getModelMapper() {
        var modelMapper = new ModelMapper();
        return modelMapper;
    }
}