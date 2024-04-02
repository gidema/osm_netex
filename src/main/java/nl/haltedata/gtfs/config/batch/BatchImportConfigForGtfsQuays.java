package nl.haltedata.gtfs.config.batch;

import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
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
import nl.haltedata.gtfs.SpecialOperatorsService;
import nl.haltedata.gtfs.dto.GtfsCsvStop;
import nl.haltedata.gtfs.dto.GtfsQuay;
import nl.haltedata.gtfs.mapping.Stop2QuayProcessor;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchImportConfigForGtfsQuays {
    public static String JOB_NAME = "gtfsQuayImportJob";

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
    FlatFileItemReader<GtfsCsvStop> reader(@Value("#{jobParameters['filePath']}") String path) {
        return new FlatFileItemReaderBuilder<GtfsCsvStop>()
                .name("gtfsQuayReader")
                .resource(new FileSystemResource(path))
                .linesToSkip(1)  // skip header row
                .delimited()
                .names(new String[]{"stop_id","stop_code","stop_name","stop_lat","stop_lon","location_type",
                        "parent_station","stop_timezone","wheelchair_boarding","platform_code","zone_id"})
                .fieldSetMapper(fieldSetMapper())
//                .prototypeBeanName(path)
                .build();
    }

    @SuppressWarnings("static-method")
    @Bean
    FieldSetMapper<GtfsCsvStop> fieldSetMapper() {
        var mapper = new BeanWrapperFieldSetMapper<GtfsCsvStop>() {{
            setTargetType(GtfsCsvStop.class);
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
    JpaItemWriter<GtfsQuay> writer() {
        var writer = new JpaItemWriter<GtfsQuay>();
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
            .<GtfsCsvStop, GtfsQuay>chunk(1000, transactionManager)
            .reader(reader(null))  // null path just for type resolution
            .processor(processor())
            .writer(writer())
//            .listener(importListener())
            .build();
    }
    
    @Bean
    Stop2QuayProcessor processor() {
        return new Stop2QuayProcessor(new GeometryFactory(), specialOperatorsService());
    }
    
    @SuppressWarnings("static-method")
    @Bean
    SpecialOperatorsService specialOperatorsService() {
        return new SpecialOperatorsService();
    }
    
    @Bean
    @JobScope
    ItemWriteListener<GtfsQuay> getItemWriteListener(){
       return importListener();
    }

    @Bean
    @JobScope
    JobExecutionListener jobExecutionListener(){
        return importListener();
    }

    @Bean
    @JobScope
    GtfsQuayImportListener importListener(){
        return  new GtfsQuayImportListener();
    }
}