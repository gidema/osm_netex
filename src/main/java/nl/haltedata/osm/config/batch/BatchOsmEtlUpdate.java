package nl.haltedata.osm.config.batch;

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
import org.springframework.boot.context.properties.bind.Name;
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
public class BatchOsmEtlUpdate {

    private static String create_routes_sql = """
DROP TABLE IF EXISTS osm_pt.osm_routes;
SELECT rt.id AS relation_id,
  CAST((rt.tags->'route') AS CHARACTER VARYING) AS transport_mode,
  CAST((rt.tags->'name') AS CHARACTER VARYING) AS "name",
  CAST((rt.tags->'operator') AS CHARACTER VARYING) AS operator,
  CAST((rt.tags->'ref') AS CHARACTER VARYING) AS route_ref,
  CAST((rt.tags->'network') AS CHARACTER VARYING) AS network,
  CAST((rt.tags->'from') AS CHARACTER VARYING) AS from,
  CAST((rt.tags->'to') AS CHARACTER VARYING) AS to
INTO osm_pt.osm_routes
FROM relations rt
WHERE rt.tags->'type' = 'route' AND rt.tags->'route'  IN ('bus', 'tram', 'train');
""";

    private static String create_quays_sql = """
DROP TABLE IF EXISTS osm_pt.osm_platform_node;
SELECT nd.id AS node_id,
  nd.geom AS wgs_location,
  ST_TRANSFORM(nd.geom, 28992) AS rd_location,
  CAST((nd.tags->'public_transport') AS character varying) AS platform_type,
  CAST((nd.tags->'name') AS character varying) AS "name",
  CAST((nd.tags->'bus') AS character varying) AS is_bus,
  CAST((nd.tags->'ref:IFOPT') AS character varying) AS ref_ifopt,
  CAST((nd.tags->'note') AS character varying) AS note
INTO osm_pt.osm_platform_node
FROM nodes nd
WHERE nd.tags->'public_transport' LIKE 'platform%';
""";

    private static String create_route_quays_sql = """
DROP TABLE IF EXISTS osm_pt.osm_route_quay;
SELECT sub.osm_route_id, 
  sub.osm_platform_id,
  sub.rank AS platform_index,
  sub.platform_type,
  sub.quay_code,
  sub.stopplace_code,
  CASE WHEN sub.rank=1 THEN 'start' WHEN sub.rank = sub.count THEN 'end' ELSE 'middle' END AS platform_location_type
INTO osm_pt.osm_route_quay
FROM (
  SELECT mb.relation_id AS osm_route_id,
    mb.member_id AS osm_platform_id,
    mb.sequence_id,
    mb.member_role AS platform_type,
    osm_quay.ref_ifopt AS quay_code,
    csp.stopplacecode AS stopplace_code,
    ROW_NUMBER() OVER (
      PARTITION BY mb.relation_id
      ORDER BY mb.sequence_id ASC
    ) AS "rank",
    COUNT(*) OVER (
      PARTITION BY mb.relation_id
    ) AS count
  FROM relation_members mb
  JOIN osm_pt.osm_routes route ON route.relation_id = mb.relation_id AND mb.member_role LIKE 'platform%' AND mb.member_type = 'N'
  LEFT JOIN osm_pt.osm_platform_node osm_quay ON osm_quay.node_id = mb.member_id
  LEFT JOIN chb.chb_quay chb_quay ON chb_quay.quaycode = osm_quay.ref_ifopt
  LEFT JOIN chb.chb_stop_place csp ON csp.id = chb_quay.stop_place_id
) AS sub;
""";

    private static String create_table_osm_route_data = """
DROP TABLE IF EXISTS osm_pt.osm_route_data;
WITH data AS (
  SELECT route.route_ref AS line_number,
    route.relation_id AS osm_route_id,
    route.network AS network,
    ARRAY_AGG(route_quay.quay_code ORDER BY route_quay.platform_index) quay_list,
    ARRAY_AGG(route_quay.stopplace_code ORDER BY route_quay.platform_index) stopplace_list,
    CAST(COUNT(route_quay.quay_code) AS INTEGER) AS quay_count
  FROM osm_pt.osm_routes AS route
  JOIN osm_pt.osm_route_quay AS route_quay ON route_quay.osm_route_id = route.relation_id
  GROUP BY route.route_ref, route.relation_id, route.network)
SELECT data.*,
    start_quay.quay_code AS start_quay_code,
    end_quay.quay_code AS end_quay_code,
    start_quay.stopplace_code AS start_stopplace_code,
    end_quay.stopplace_code AS end_stopplace_code
INTO osm_pt.osm_route_data
FROM data
JOIN osm_pt.osm_route_platform start_quay ON start_quay.osm_route_id = data.osm_route_id AND start_quay.platform_location_type = 'start'
JOIN osm_pt.osm_route_platform end_quay ON end_quay.osm_route_id = data.osm_route_id AND end_quay.platform_location_type = 'end';
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
    Job updateOsmQuaysJob(JobRepository jobRepository, Step createRoutesStep,
            @Qualifier("createQuaysStep") Step createQuaysStep, @Qualifier("createRouteQuaysStep") Step createRouteQuaysStep,
            @Qualifier("createRouteDataStep") Step createRouteDataStep)  {

        return new JobBuilder("osmEtlUpdate", jobRepository)
            .start(createRoutesStep)
            .next(createQuaysStep)
            .next(createRouteQuaysStep)
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
    Step createRoutesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            Tasklet osmRouteBuilder) {
        return new StepBuilder("createRoutesStep", jobRepository)
        .tasklet(osmRouteBuilder, transactionManager)
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
    Step createQuaysStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            Tasklet osmQuayBuilder) {
        return new StepBuilder("createQuaysStep", jobRepository)
        .tasklet(osmQuayBuilder, transactionManager)
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
    Step createRouteQuaysStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            Tasklet osmRouteQuayBuilder) {
        return new StepBuilder("createRouteQuaysStep", jobRepository)
        .tasklet(osmRouteQuayBuilder, transactionManager)
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
            Tasklet osmRouteDataBuilder) {
        return new StepBuilder("createRouteDataStep", jobRepository)
        .tasklet(osmRouteDataBuilder, transactionManager)
        .allowStartIfComplete(true)
        .build();
    }

    @SuppressWarnings("static-method")
    @Bean 
    TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);       
    }
    
    @Bean
    Tasklet osmRouteBuilder(TransactionTemplate transactionTemplate) {
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
                          .createNativeQuery(create_routes_sql)
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
    Tasklet osmQuayBuilder(TransactionTemplate transactionTemplate) {
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
                          .createNativeQuery(create_quays_sql)
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
    
    @Bean
    Tasklet osmRouteQuayBuilder(TransactionTemplate transactionTemplate) {
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
                          .createNativeQuery(create_route_quays_sql)
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
    
    @Bean
    Tasklet osmRouteDataBuilder(TransactionTemplate transactionTemplate) {
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
                          .createNativeQuery(create_table_osm_route_data)
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