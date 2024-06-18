package nl.haltedata.gtfs.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nl.haltedata.gtfs.config.batch.BatchExportConfigForOsmGtfsQuays;
import nl.haltedata.gtfs.config.batch.BatchImportConfigForGtfsAgencies;
import nl.haltedata.gtfs.config.batch.BatchImportConfigForGtfsQuays;

@Configuration
@EnableBatchProcessing(modular = true)
public class GtfsBatchConfig {
    @SuppressWarnings("static-method")
	@Bean
    ApplicationContextFactory importGtfsAgenciesConfig() {
        return new GenericApplicationContextFactory(BatchImportConfigForGtfsAgencies.class);
    }
    
    @SuppressWarnings("static-method")
	@Bean
    ApplicationContextFactory importGtsfQuaysConfig() {
        return new GenericApplicationContextFactory(BatchImportConfigForGtfsQuays.class);
    }

    @SuppressWarnings("static-method")
	@Bean
    ApplicationContextFactory exportGtsfQuays2OsmConfig() {
        return new GenericApplicationContextFactory(BatchExportConfigForOsmGtfsQuays.class);
    }
}
