package nl.haltedata.netex.config.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import nl.haltedata.netex.dto.NetexFileInfo;
import nl.haltedata.netex.ndov.NdovNetexFileInfoReader;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchImportConfigForNetexFileInfo {

    private final EntityManagerFactory entityManagerFactory;

    /**
     * Creates and returns a {@link ItemReader} bean for NetexFileInfo entities.
     *
     * @return a configured ItemReader.
     */
    @Bean
    static
    NdovNetexFileInfoReader reader() {
        return new NdovNetexFileInfoReader();
    }
    
    /**
     * Creates and returns a {@link JpaItemWriter} bean for persisting entities.
     *
     * @return a configured JpaItemWriter for writing entities.
     */
    @Bean
    JpaItemWriter<NetexFileInfo> writer() {
        var writer = new JpaItemWriter<NetexFileInfo>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
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
        return new JobBuilder("importNetexFileInfoJob", jobRepository)
            .start(step1)
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
    Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
            .<NetexFileInfo, NetexFileInfo>chunk(1000, transactionManager)
            .reader(reader())  // null path just for type resolution
            .writer(writer())
            .allowStartIfComplete(true)
            .build();
    }
}