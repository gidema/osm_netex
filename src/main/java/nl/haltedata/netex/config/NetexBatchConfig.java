package nl.haltedata.netex.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nl.haltedata.netex.config.batch.BatchExportConfigForOsmNetexQuays;
import nl.haltedata.netex.config.batch.BatchImportConfigForNetexFileInfo;
import nl.haltedata.netex.config.batch.BatchImportConfigForNetexLines;
import nl.haltedata.netex.config.batch.BatchImportConfigForNetexRoutePoints;
import nl.haltedata.netex.config.batch.BatchImportConfigForNetexRoutes;
import nl.haltedata.netex.config.batch.BatchImportConfigForNetexScheduledStopPoints;

@Configuration
@EnableBatchProcessing(modular = true)
public class NetexBatchConfig {
    @SuppressWarnings("static-method")
    @Bean
    ApplicationContextFactory importNetexScheduledStopPointsConfig() {
        return new GenericApplicationContextFactory(BatchImportConfigForNetexScheduledStopPoints.class);
    }
    
    @SuppressWarnings("static-method")
    @Bean
    ApplicationContextFactory importNetexRoutePointsConfig() {
        return new GenericApplicationContextFactory(BatchImportConfigForNetexRoutePoints.class);
    }
    
    @SuppressWarnings("static-method")
    @Bean
    ApplicationContextFactory importNetexlinesConfig() {
        return new GenericApplicationContextFactory(BatchImportConfigForNetexLines.class);
    }
    
    @SuppressWarnings("static-method")
    @Bean
    ApplicationContextFactory importNetexRoutesConfig() {
        return new GenericApplicationContextFactory(BatchImportConfigForNetexRoutes.class);
    }
    
    @SuppressWarnings("static-method")
    @Bean
    ApplicationContextFactory importNetexFileInfoConfig() {
        return new GenericApplicationContextFactory(BatchImportConfigForNetexFileInfo.class);
    }
    
    @SuppressWarnings("static-method")
    @Bean
    ApplicationContextFactory exportNetexOsmQuays() {
        return new GenericApplicationContextFactory(BatchExportConfigForOsmNetexQuays.class);
    } 
}
