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
import nl.chb.psa.Quay;
import nl.haltedata.chb.ChbPsaReader;
import nl.haltedata.chb.dto.ChbPsa;
import nl.haltedata.chb.dto.ChbPsaRepository;
import nl.haltedata.chb.mapping.ChbPsaMapper;
import nl.haltedata.tools.GzipFileSystemResource;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchImportConfigForChbPsa {
    private static Path filePath = Path.of("/home/gertjan/projects/NLGeo/Haltedata/chb/PassengerStopAssignmentExportCHB20240507013913.xml.gz"); 

    public static String JOB_NAME = "chbPsaImportJob";

    private final EntityManagerFactory entityManagerFactory;


    @Bean
//    @StepScope
    ChbPsaReader psaReader() {
        return new ChbPsaReader(quayReader(), psaMapper());
    }
    
    @SuppressWarnings("static-method")
    @Bean
    ChbPsaMapper psaMapper() {
        return new ChbPsaMapper();
    }
    
    @Bean
    @StepScope
    StaxEventItemReader<Quay> quayReader() {
        return new StaxEventItemReaderBuilder<Quay>()
            .name("chbPsaQuayReader")
            .resource(new GzipFileSystemResource(filePath))
            .addFragmentRootElements("quay")
            .unmarshaller(stopplaceMarshaller())
            .build();
    }

    @SuppressWarnings("static-method")
    @Bean
    Jaxb2Marshaller stopplaceMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Quay.class);
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
    JpaItemWriter<ChbPsa> writer() {
        var writer = new JpaItemWriter<ChbPsa>();
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
            .tasklet(psaTruncater(), transactionManager)
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
            .<ChbPsa, ChbPsa>chunk(1000, transactionManager)
            .reader(psaReader())  // null path just for type resolution
            .writer(writer())
            .build();
    }

    @Bean
    Tasklet psaTruncater() {
        return new PsaTruncater();
    }

//    @SuppressWarnings("static-method")
//    @Bean
//    ChbQuayProcessor processor(Supplier<Stopplace> stopplaceSupplier) {
//        return new ChbQuayProcessor(stopplaceSupplier);
//    }
    
    class PsaTruncater implements Tasklet, StepExecutionListener {

        private ChbPsaRepository psaRepository;
        
        @Inject
        public void setPsaRepository(ChbPsaRepository psaRepository) {
            this.psaRepository = psaRepository;
        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
            psaRepository.deleteAll();
        }

        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            return null;
        }
        
    }
}