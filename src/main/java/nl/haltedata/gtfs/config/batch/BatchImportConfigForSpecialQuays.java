package nl.haltedata.gtfs.config.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import nl.haltedata.gtfs.dto.GtfsSpecialQuay;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchImportConfigForSpecialQuays {

    private final EntityManagerFactory entityManagerFactory;

    /**
     * Creates and returns a {@link FlatFileItemReader} bean for reading CSV records.
     * The reader uses job parameters to determine the file path at runtime.
     *
     * @param path the file path provided at runtime through job parameters.
     * @return a configured FlatFileItemReader for reading Contact entities.
     */
    @Bean
    @StepScope
    FlatFileItemReader<GtfsSpecialQuay> reader(@Value("#{jobParameters['filePath']}") String path) {
        return new FlatFileItemReaderBuilder<GtfsSpecialQuay>()
                .name("gtfsSpecialQuayReader")
                .resource(new FileSystemResource(path))
                .linesToSkip(1)  // skip header row
                .delimited()
                .names(new String[]{"gtfs_id", "operator"})
                .fieldSetMapper(fieldSetMapper())
                .build();

    }

    @Bean
    @SuppressWarnings("static-method")
    FieldSetMapper<GtfsSpecialQuay> fieldSetMapper() {
        var mapper = new BeanWrapperFieldSetMapper<GtfsSpecialQuay>() {{
            setTargetType(GtfsSpecialQuay.class);
        }};
        return mapper;
    }

    /**
     * Defines the main batch job for importing.
     *
     * @param jobRepository the repository for storing job metadata.
     * @param step1 the step associated with this job.
     * @return a configured Job for importing contacts.
     */
    @SuppressWarnings("static-method")
    @Bean
    Job importJob(JobRepository jobRepository, Step step1)  {
        return new JobBuilder("gtfsSpecialQuayImportJob", jobRepository)
            .start(step1)
            .build();
    }

    /**
     * Creates and returns a {@link JpaItemWriter} bean for persisting entities.
     *
     * @return a configured JpaItemWriter for writing entities.
     */
    @Bean
    JpaItemWriter<GtfsSpecialQuay> writer() {
        JpaItemWriter<GtfsSpecialQuay> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
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
            .<GtfsSpecialQuay, GtfsSpecialQuay>chunk(1000, transactionManager)
            .reader(reader(null))  // null path just for type resolution
            .writer(writer())
            .build();
    }
}