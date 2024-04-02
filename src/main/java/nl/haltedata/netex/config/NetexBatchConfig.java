package nl.haltedata.netex.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nl.haltedata.netex.config.batch.BatchExportConfigForOsmNextexQuays;
import nl.haltedata.netex.config.batch.BatchImportConfigForNetexFileInfo;
import nl.haltedata.netex.config.batch.BatchImportConfigForNetexScheduledStopPoints;

@Configuration
@EnableBatchProcessing(modular = true)
public class NetexBatchConfig {
    @Bean
    ApplicationContextFactory importNetexScheduledStopPointsConfig() {
        return new GenericApplicationContextFactory(BatchImportConfigForNetexScheduledStopPoints.class);
    }
    
    @Bean
    ApplicationContextFactory importNetexFileInfoConfig() {
        return new GenericApplicationContextFactory(BatchImportConfigForNetexFileInfo.class);
    }
    
    @Bean
    ApplicationContextFactory exportNetexOsmQuays() {
        return new GenericApplicationContextFactory(BatchExportConfigForOsmNextexQuays.class);
    }
    
    
}
