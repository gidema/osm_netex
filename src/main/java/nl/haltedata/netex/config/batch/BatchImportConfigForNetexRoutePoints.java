package nl.haltedata.netex.config.batch;

import java.io.IOException;
import java.util.Arrays;

import org.rutebanken.netex.model.Route;
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
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import nl.haltedata.netex.NetexPointOnRouteReader;
import nl.haltedata.netex.dto.NetexPointOnRoute;
import nl.haltedata.netex.dto.NetexPointOnRouteRepository;
import nl.haltedata.netex.mapping.PointOnRouteMapper;
import nl.haltedata.tools.GzipAwareMultiResourceItemReader;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchImportConfigForNetexRoutePoints {

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    @StepScope
    ItemReader<NetexPointOnRoute> multiResourceReader(@Value("#{jobParameters['filePath']}") String path) {
        var patternResolver = new PathMatchingResourcePatternResolver();   
        try {
            Resource[] resources = patternResolver.getResources("file:" + path + "/*.xml.gz");
//            resources = Arrays.copyOfRange(resources, 0, 1);
            var reader = new GzipAwareMultiResourceItemReader<NetexPointOnRoute>();
            reader.setResources(resources);
            reader.setDelegate(pointOnRouteReader());
            return reader;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Creates and returns a {@link JpaItemWriter} bean for persisting entities.
     *
     * @return a configured JpaItemWriter for writing entities.
     */
    @Bean
    JpaItemWriter<NetexPointOnRoute> writer() {
        var writer = new JpaItemWriter<NetexPointOnRoute>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    @StepScope
    NetexPointOnRouteReader pointOnRouteReader() {
        return new NetexPointOnRouteReader(routeReader(), pointOnRouteMapper());
    }
    
    @SuppressWarnings("static-method")
    @Bean
    PointOnRouteMapper pointOnRouteMapper() {
        return new PointOnRouteMapper();
    }
    
    @Bean
    @StepScope
    StaxEventItemReader<Route> routeReader() {
        return new StaxEventItemReaderBuilder<Route>()
            .name("netexRouteReader")
//            .resource(new GzipFileSystemResource(path))
            .addFragmentRootElements("Route")
            .unmarshaller(routeMarshaller())
            .build();
    }

    @SuppressWarnings("static-method")
    @Bean
    Jaxb2Marshaller routeMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setMappedClass(Route.class);
        marshaller.setClassesToBeBound(Route.class);
        return marshaller;
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
        return new JobBuilder("importNetexRoutePointJob", jobRepository)
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
            .tasklet(routePointTruncater(), transactionManager)
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
            .<NetexPointOnRoute, NetexPointOnRoute>chunk(1000, transactionManager)
            .reader(multiResourceReader(null))  // null path just for type resolution
            .writer(writer())
            .build();
    }
    
    @Bean
    Tasklet routePointTruncater() {
        return new RoutePointTruncater();
    }

    class RoutePointTruncater implements Tasklet, StepExecutionListener {

        private NetexPointOnRouteRepository repository;
        
        @Inject
        public void setRepository(NetexPointOnRouteRepository repository) {
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