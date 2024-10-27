package nl.haltedata.chb.config.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class ChbEtlUpdateBatch {

    private static String create_dim_chb_quay_sql = """
TRUNCATE TABLE chb.dim_chb_quay;
INSERT INTO chb.dim_chb_quay (
  SELECT q.quaycode AS quay_code,
    s.town AS place,
    q.quay_name,
    q.stop_side_code,
    s.stopplacecode AS area_code,
    q.quay_status,
    q.quay_type,
    s.stopplacetype AS area_type,
    q.bearing
  FROM chb.chb_quay q
    LEFT JOIN chb.chb_stop_place s ON s.id = q.stop_place_id
);
""";

    private final EntityManagerFactory entityManagerFactory;

    @Inject
    private ApplicationContext applicationContext;

    @Bean
    EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
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
    Job updateOsmQuaysJob(JobRepository jobRepository, 
            @Qualifier("createDimChbQuayStep") Step createDimChbQuayStep)  {

        return new JobBuilder("chbEtlUpdate", jobRepository)
            .start(createDimChbQuayStep)
            .build();
    }

    /**
     * Defines the step to update the osm_route table.
     *
     * @param jobRepository the repository for storing job metadata.
     * @param transactionManager the transaction manager to handle transactional behavior.
     * @return a configured Step.
     */
    @Bean
    Step createDimChbQuayStep(JobRepository jobRepository,
            Tasklet cimChbQuayBuilder) {
        var transactionManager = applicationContext.getBean(PlatformTransactionManager.class);
        return new StepBuilder("createDimChbQuayStep", jobRepository)
        .tasklet(cimChbQuayBuilder, transactionManager)
        .allowStartIfComplete(true)
        .build();
    }


    @SuppressWarnings("static-method")
    @Bean 
    TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);       
    }
    
    @Bean
    Tasklet dimChbQuayBuilder(TransactionTemplate transactionTemplate) {
        return sqlTasklet(transactionTemplate, create_dim_chb_quay_sql);
    }
    
    private Tasklet sqlTasklet(TransactionTemplate transactionTemplate, String query) {
        return new Tasklet() {

            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                try (
                        var entityManager = entityManagerFactory.createEntityManager();
                )
                {
                    transactionTemplate.execute(transactionStatus -> {
                        entityManager.joinTransaction();
                        entityManager
                          .createNativeQuery(query)
                          .executeUpdate();
                        transactionStatus.flush();
                        return null;
                    });
                }
                finally {
                    //
                }
                return null;
            }
            
        };
    }
}