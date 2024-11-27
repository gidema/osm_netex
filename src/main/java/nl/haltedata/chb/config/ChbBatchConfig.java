package nl.haltedata.chb.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nl.haltedata.chb.config.batch.BatchExportConfigForOsmChbQuays;
import nl.haltedata.chb.config.batch.ChbEtlUpdateBatch;

@Configuration
@EnableBatchProcessing(modular = true)
public class ChbBatchConfig {

    @Bean
    static ApplicationContextFactory exportChbToOsmConfig() {
        return new GenericApplicationContextFactory(BatchExportConfigForOsmChbQuays.class);
    }
    
    @Bean
    static ApplicationContextFactory batchChbEtlUpdateConfig() {
        return new GenericApplicationContextFactory(ChbEtlUpdateBatch.class);
    }

}
