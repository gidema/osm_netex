package nl.haltedata.netex.config.batch;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class NetexEtlUpdateConfig {

    private static String create_netex_route_quay_sql = """
DROP TABLE IF EXISTS netex.netex_route_quay;
SELECT sub.public_code AS line_number,
  sub.route_id,
  sub.quay_code,
  sub.stopplace_code,
  sub.rank AS quay_index,
  CASE WHEN sub.rank=1 THEN 'start' WHEN sub.rank = sub.count THEN 'end' ELSE 'middle' END AS quay_location_type    
INTO netex.netex_route_quay
    FROM (
    SELECT
    line.public_code,
    por.route_id,
    psa.quay_code,
    COALESCE(psa.stopplace_code, csp.stopplacecode) AS stopplace_code,
    ROW_NUMBER() OVER (
      PARTITION BY por.route_id
      ORDER BY por.sequence ASC
    ) AS "rank",
    COUNT(*) OVER (
      PARTITION BY por.route_id
    ) AS count

  FROM netex.netex_point_on_route por
  JOIN netex.netex_quay quay ON quay.route_point_ref = por.route_point_ref
  JOIN netex.netex_route route ON route.id = por.route_id
  JOIN netex.netex_line line ON route.line_ref = line.id
  LEFT JOIN chb.chb_psa psa ON psa.user_stop_owner_code = quay.user_stop_owner_code
      AND psa.user_stop_code = quay.user_stop_code
  LEFT JOIN chb.chb_quay ON chb_quay.quaycode = psa.quay_code
  LEFT JOIN chb.chb_stop_place csp ON csp.id = chb_quay.stop_place_id
  ORDER BY por.route_id, por.sequence) AS sub
""";

    private static String create_netex_route_data_sql = """
DROP TABLE IF EXISTS netex.netex_route_data;
WITH stats AS (
  SELECT route_quay.line_number, 
    route_quay.route_id, 
    ARRAY_AGG(route_quay.quay_code ORDER BY route_quay.quay_index) quay_list,
    ARRAY_AGG(route_quay.stopplace_code ORDER BY route_quay.quay_index) stopplace_list,
    COUNT(route_quay.quay_code) AS quay_count
  FROM netex.netex_route_quay route_quay
  GROUP BY route_quay.line_number, route_quay.route_id)
SELECT stats.*,
    start_quay.quay_code AS start_quay_code,
    end_quay.quay_code AS end_quay_code,
    start_quay.stopplace_code AS start_stopplace_code,
    end_quay.stopplace_code AS end_stopplace_code
INTO netex.netex_route_data
FROM stats
JOIN netex.netex_route_quay start_quay ON start_quay.route_id = stats.route_id AND start_quay.quay_location_type = 'start'
JOIN netex.netex_route_quay end_quay ON end_quay.route_id = stats.route_id AND end_quay.quay_location_type = 'end';
""";

    private final EntityManagerFactory entityManagerFactory;

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
    Job netexEtlUpdate(JobRepository jobRepository,
            @Qualifier("createRouteQuaysStep") Step createRouteQuaysStep,
            @Qualifier("createRouteDataStep") Step createRouteDataStep)  {

        return new JobBuilder("netexEtlUpdateJob", jobRepository)
            .start(createRouteQuaysStep)
            .next(createRouteDataStep)
            .build();
    }

    /**
     * Defines the step to update the osm_route table.
     *
     * @param jobRepository the repository for storing job metadata.
     * @param transactionManager the transaction manager to handle transactional behavior.
     * @return a configured Step.
     */
    @SuppressWarnings("static-method")
    @Bean
    Step createRouteQuaysStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            Tasklet netexRouteQuayBuilder) {
        return new StepBuilder("createRouteQuaysStep", jobRepository)
        .tasklet(netexRouteQuayBuilder, transactionManager)
        .allowStartIfComplete(true)
        .build();
    }

    /**
     * Defines the step to update the osm_route table.
     *
     * @param jobRepository the repository for storing job metadata.
     * @param transactionManager the transaction manager to handle transactional behavior.
     * @return a configured Step.
     */
    @SuppressWarnings("static-method")
    @Bean
    Step createRouteDataStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            Tasklet netexRouteDataBuilder) {
        return new StepBuilder("createRouteDataStep", jobRepository)
        .tasklet(netexRouteDataBuilder, transactionManager)
        .allowStartIfComplete(true)
        .build();
    }

    @SuppressWarnings("static-method")
    @Bean 
    TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);       
    }
    
    @Bean
    Tasklet netexRouteQuayBuilder(TransactionTemplate transactionTemplate) {
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
                          .createNativeQuery(create_netex_route_quay_sql)
                          .executeUpdate();
                        transactionStatus.flush();
                        return RepeatStatus.FINISHED;
                    });
                }
                finally {
                    //
                }
                return RepeatStatus.FINISHED;
            }
            
        };
    }
    
    @Bean
    Tasklet netexRouteDataBuilder(TransactionTemplate transactionTemplate) {
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
                          .createNativeQuery(create_netex_route_data_sql)
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