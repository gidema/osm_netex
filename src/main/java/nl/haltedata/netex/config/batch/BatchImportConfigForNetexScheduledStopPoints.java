package nl.haltedata.netex.config.batch;

import java.io.IOException;

import org.rutebanken.netex.model.ScheduledStopPoint;
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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import nl.haltedata.netex.dto.NetexQuay;
import nl.haltedata.netex.dto.NetexQuayRepository;
import nl.haltedata.netex.mapping.NetexQuayProcessor;
import nl.haltedata.tools.GzipFileSystemResource;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchImportConfigForNetexScheduledStopPoints {

    private final EntityManagerFactory entityManagerFactory;

//    /**
//     * Creates and returns a {@link ItemReader} bean for netex scheduledStopPoint entities.
//     *
//     * @return a configured JpaItemReader.
//     */
//    @Bean
//    @StepScope
//    StaxEventItemReader<JAXBElement<ScheduledStopPoint>> reader(@Value("#{jobParameters['filePath']}") String path) {
//        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
//        marshaller.setClassesToBeBound(ScheduledStopPoint.class);
// 
//        return new StaxEventItemReaderBuilder<JAXBElement<ScheduledStopPoint>>()
//                .name("scheduledStopPointReader")
//                .resource(new FileSystemResource(path))
//                .addFragmentRootElements("ScheduledStopPoint")
//                .unmarshaller(marshaller)
//                .build();
//    }
    
    @Bean
    @StepScope
    ItemReader<JAXBElement<ScheduledStopPoint>> reader(@Value("#{jobParameters['filePath']}") String path) {
        Resource[] compressedResources = null;
        Resource[] uncompressedResources = null;
        var patternResolver = new PathMatchingResourcePatternResolver();   
        try {
            compressedResources = patternResolver.getResources("file:" + path + "/*.xml.gz");
            uncompressedResources = new Resource[compressedResources.length];
            for (int i =0; i < compressedResources.length; i++) {
                uncompressedResources[i] = new GzipFileSystemResource(compressedResources[i].getFile().toPath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MultiResourceItemReader<JAXBElement<ScheduledStopPoint>> reader = new MultiResourceItemReader<>();
        reader.setResources(uncompressedResources);
        reader.setDelegate(createEventItemReader());
        return reader;
    }
    
    private static StaxEventItemReader<JAXBElement<ScheduledStopPoint>> createEventItemReader() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ScheduledStopPoint.class);
        return new StaxEventItemReaderBuilder<JAXBElement<ScheduledStopPoint>>()
           .name("scheduledStopPointReader")
           .addFragmentRootElements("ScheduledStopPoint")
           .unmarshaller(marshaller)
           .build();
    }
    
    JpaCursorItemReader<ScheduledStopPoint> _reader() {
        JpaCursorItemReader<ScheduledStopPoint> reader = new JpaCursorItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        return reader;
    }
    /**
     * Creates and returns a {@link JpaItemWriter} bean for persisting entities.
     *
     * @return a configured JpaItemWriter for writing entities.
     */
    @Bean
    JpaItemWriter<NetexQuay> writer() {
        var writer = new JpaItemWriter<NetexQuay>();
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
    Job importJob(JobRepository jobRepository, Step step1, Step step2)  {
        return new JobBuilder("importNetexScheduledStopPointJob", jobRepository)
            .start(step1)
            .next(step2)
            .build();
    }

    /**
     * Step to remove existing quays from the database.
     *
     * @param jobRepository the repository for storing job metadata.
     * @param transactionManager the transaction manager to handle transactional behavior.
     * @return a configured Step for reading and writing Contact entities.
     */
    @Bean
    Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
            .tasklet(quayTruncater(), transactionManager)
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
            .<JAXBElement<ScheduledStopPoint>, NetexQuay>chunk(1000, transactionManager)
            .reader(reader(null))  // null path just for type resolution
            .processor(processor())
            .writer(writer())
            .build();
    }
    
    @Bean
    Tasklet quayTruncater() {
        return new QuayTruncater();
    }

    @Bean
    NetexQuayProcessor processor() {
        return new NetexQuayProcessor();
    }
    
    class QuayTruncater implements Tasklet, StepExecutionListener {

        private NetexQuayRepository quayRepository;
        
        @Inject
        public void setQuayRepository(NetexQuayRepository quayRepository) {
            this.quayRepository = quayRepository;
        }

        @Override
        public void beforeStep(StepExecution stepExecution) {
            quayRepository.deleteAll();
            int i=0;
        }

        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            return null;
        }
        
    }
}