package nl.haltedata.osm.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nl.haltedata.osm.config.batch.BatchOsmEtlUpdate;

@Configuration
@EnableBatchProcessing(modular = true)
public class OsmBatchConfig {
    @SuppressWarnings("static-method")
    @Bean
    ApplicationContextFactory batchOsmEtlUpdateConfig() {
        return new GenericApplicationContextFactory(BatchOsmEtlUpdate.class);
    }
    
}
