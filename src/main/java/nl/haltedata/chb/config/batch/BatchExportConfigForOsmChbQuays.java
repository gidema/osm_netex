package nl.haltedata.chb.config.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import nl.haltedata.chb.dto.ChbQuay;
import nl.haltedata.chb.osm.ChbQuayNodeFactory;
import nl.haltedata.osm.OsmNodeItemWriter;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchExportConfigForOsmChbQuays {

    private final EntityManagerFactory entityManagerFactory;

    /**
     * Creates and returns a {@link JpaItemReader} bean for persisted netex Quay entities.
     *
     * @return a configured JpaItemReader.
     */
    JpaCursorItemReader<ChbQuay> reader() {
        JpaCursorItemReader<ChbQuay> reader = new JpaCursorItemReader<>();
        reader.setQueryString("from ChbQuay");
        reader.setEntityManagerFactory(entityManagerFactory);
        return reader;
    }


    /**
     * Defines the main batch job for exporting.
     *
     * @param jobRepository the repository for storing job metadata.
     * @param step1 the step associated with this job.
     * @return a configured Job for exporting quays.
     */
    @SuppressWarnings("static-method")
    @Bean
    Job exportJob(JobRepository jobRepository, Step step1)  {
        return new JobBuilder("exportChbToOsmJob", jobRepository)
            .start(step1)
            .build();
    }

    /**
     * Creates and returns a {@link OsmNodeItemWriter} bean for persisting entities.
     *
     * @return a configured OsmNodeItemWriter for writing entities.
     */
    @Bean
    @StepScope
    OsmNodeItemWriter<ChbQuay> writer(@Value("#{jobParameters['filePath']}") String path) {
        OsmNodeItemWriter<ChbQuay> writer = new OsmNodeItemWriter<>(quayNodeFactory());
        FileSystemResource osmFile = new FileSystemResource(path);
        writer.setResource(osmFile);
        return writer;
    }
    
    @SuppressWarnings("static-method")
    @Bean
    ChbQuayNodeFactory quayNodeFactory() {
        return new ChbQuayNodeFactory();
    }

    /**
     * Defines the main batch step which includes reading, processing (if any), and writing.
     *
     * @param jobRepository the repository for storing job metadata.
     * @param transactionManager the transaction manager to handle transactional behavior.
     * @return a configured Step for reading and writing Contact entities.
     */
    @Bean
    Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
            .<ChbQuay, ChbQuay>chunk(1000, transactionManager)
            .reader(reader())
            .processor(new QuayFilter())
            .writer(writer(null)) // null path just for type resolution
            .build();
    }
    
    /**
     * Filter CHB quays. Ignore expired quays
     */
    private class QuayFilter implements ItemProcessor<ChbQuay, ChbQuay> {
        @Override
        public ChbQuay process(ChbQuay quay) throws Exception {
            return "expired".equals(quay.getQuayStatus()) ? null : quay;
        }
        
    }
}