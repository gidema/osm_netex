package nl.haltedata.netex.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nl.haltedata.netex.config.batch.BatchExportConfigForOsmNetexQuays;

@Configuration
@EnableBatchProcessing(modular = true)
public class NetexBatchConfig {
    @SuppressWarnings("static-method")
    @Bean
    ApplicationContextFactory exportNetexOsmQuays() {
        return new GenericApplicationContextFactory(BatchExportConfigForOsmNetexQuays.class);
    } 
}
