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
import nl.haltedata.gtfs.dto.GtfsAgency;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchImportConfigForGtfsAgencies {
    public static String JOB_NAME = "gtfsAgencyImportJob";

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
    FlatFileItemReader<GtfsAgency> reader(@Value("#{jobParameters['filePath']}") String path) {
        return new FlatFileItemReaderBuilder<GtfsAgency>()
                .name("gtfsAgencyReader")
                .resource(new FileSystemResource(path))
                .linesToSkip(1)  // skip header row
                .delimited()
                .names(new String[]{"agency_id","agency_name","agency_url","agency_timezone","agency_phone"})
                .fieldSetMapper(fieldSetMapper())
//                .prototypeBeanName(path)
                .build();
    }

    @SuppressWarnings("static-method")
    @Bean
    FieldSetMapper<GtfsAgency> fieldSetMapper() {
        var mapper = new BeanWrapperFieldSetMapper<GtfsAgency>() {{
            setTargetType(GtfsAgency.class);
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
        var job = new JobBuilder(JOB_NAME, jobRepository)
//            .listener(importListener())
            .start(step1)
            .build();
        return job;
    }

    /**
     * Creates and returns a {@link JpaItemWriter} bean for persisting entities.
     *
     * @return a configured JpaItemWriter for writing entities.
     */
    @Bean
    JpaItemWriter<GtfsAgency> writer() {
        var writer = new JpaItemWriter<GtfsAgency>();
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
            .<GtfsAgency, GtfsAgency>chunk(1000, transactionManager)
            .reader(reader(null))  // null path just for type resolution
            .writer(writer())
//            .listener(importListener())
            .build();
    }
}