package nl.haltedata.chb.config.batch;

import java.nio.file.Path;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import nl.chb.Stopplace;
import nl.haltedata.chb.dto.ChbStopPlace;
import nl.haltedata.chb.dto.ChbStopplaceRepository;
import nl.haltedata.chb.mapping.ChbStopPlaceProcessor;
import nl.haltedata.chb.mapping.StopPlaceMapper;
import nl.haltedata.tools.GzipFileSystemResource;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchImportConfigForChbStopplace {
    private static Path filePath = Path.of("/home/gertjan/projects/NLGeo/Haltedata/chb/ExportCHB20240507013819.xml.gz"); 

    public static String JOB_NAME = "chbStopPlaceImportJob";

    private final EntityManagerFactory entityManagerFactory;

    @SuppressWarnings("static-method")
    @Bean
    StopPlaceMapper quayMapper() {
        return new StopPlaceMapper();
    }
    
    @Bean
    @StepScope
    StaxEventItemReader<Stopplace> stopplaceReader() {
        return new StaxEventItemReaderBuilder<Stopplace>()
            .name("chbStopplaceReader")
            .resource(new GzipFileSystemResource(filePath))
            .addFragmentRootElements("stopplace")
            .unmarshaller(stopplaceMarshaller())
            .build();
    }

    @SuppressWarnings("static-method")
    @Bean
    Jaxb2Marshaller stopplaceMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Stopplace.class);
        return marshaller;
    }
    
    /**
     * Defines the main batch job for importing.
     *
     * @param jobRepository the repository for storing job metadata.
     * @param step1 the step associated with this job.
     * @return a configured Job for importing contacts.
     */
    @Bean
    Job importJob(JobRepository jobRepository, PlatformTransactionManager transactionManager)  {
        var job = new JobBuilder(JOB_NAME, jobRepository)
            .start(truncateStep(jobRepository, transactionManager))
            .next(step2(jobRepository, transactionManager))
            .build();
        return job;
    }

    /**
     * Creates and returns a {@link JpaItemWriter} bean for persisting entities.
     *
     * @return a configured JpaItemWriter for writing entities.
     */
    @Bean
    JpaItemWriter<ChbStopPlace> writer() {
        var writer = new JpaItemWriter<ChbStopPlace>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    /**
     * Defines the truncate step which removes existing chb quays from the database.
     *
     * @param jobRepository the repository for storing job metadata.
     * @param transactionManager the transaction manager to handle transactional behavior.
     * @return a configured Step for reading and writing Contact entities.
     */
    @Bean
    Step truncateStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("truncateStep", jobRepository)
            .tasklet(stopplaceTruncater(), transactionManager)
            .build();
    }

    /**
     * Defines the main batch step which includes reading, processing (if any), and writing.
     *
     * @param jobRepository the repository for storing job metadata.
     * @param transactionManager the transaction manager to handle transactional behavior.
     * @return a configured Step for reading and writing Contact entities.
     */
    @Bean
    Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
            .<Stopplace, ChbStopPlace>chunk(1000, transactionManager)
            .reader(stopplaceReader())  // null path just for type resolution
            .processor(processor())
            .writer(writer())
            .build();
    }

    @SuppressWarnings("static-method")
    @Bean
    ChbStopPlaceProcessor processor() {
        return new ChbStopPlaceProcessor();
    }

    @Bean
    Tasklet stopplaceTruncater() {
        return new StopPlaceTruncater();
    }

    class StopPlaceTruncater implements Tasklet, StepExecutionListener {

        private ChbStopplaceRepository repository;
        
        @Inject
        public void setQuayRepository(ChbStopplaceRepository repository) {
            this.repository = repository;
        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
            repository.deleteAll();
        }

        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            return null;
        }
        
    }
}