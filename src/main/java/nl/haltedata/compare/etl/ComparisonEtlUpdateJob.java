package nl.haltedata.compare.etl;

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
public class ComparisonEtlUpdateJob {

    private static String update_osm_netex_line_match_table_sql = """
TRUNCATE TABLE osm_netex_line_match;
WITH match AS (
  SELECT DISTINCT orme.line_number, orme.osm_route_master_id, nle.netex_line_id
  FROM osm_pt.osm_route_master_endpoint orme
  JOIN netex.netex_line_endpoint nle ON nle.line_number = orme.line_number AND nle.stop_place_code = orme.stop_place_code)
INSERT INTO osm_netex_line_match
SELECT match.osm_route_master_id, match.netex_line_id, ol.name AS osm_line_name, nl.name AS netex_line_name, ol.network AS osm_network, osmn.country_code AS osm_country_code, match.line_number
FROM match 
  LEFT JOIN osm_pt.osm_route_master ol ON ol.osm_route_master_id = match.osm_route_master_id
  LEFT JOIN netex.netex_line nl ON nl.id = match.netex_line_id
  LEFT JOIN osm_pt.osm_pt_network osmn ON ol.network = osmn.network_name;
""";

    private static String update_all_lines_table_sql = """
    -- Clear the table
    TRUNCATE TABLE all_lines;
    -- Add lines with a match between Netex and OSM
    WITH match AS (
      SELECT DISTINCT orme.line_number, orme.osm_route_master_id, nle.netex_line_id
      FROM osm_pt.osm_route_master_endpoint orme
      JOIN netex.netex_line_endpoint nle ON nle.line_number = orme.line_number AND nle.stop_place_code = orme.stop_place_code)
    INSERT INTO all_lines(netex_line_id, osm_line_id, network, transport_mode, country_code, line_ref, product_category)
    SELECT match.netex_line_id, match.osm_route_master_id, ol.network, ol.transport_mode, osmn.country_code, match.line_number, nl.product_category
    FROM match 
      LEFT JOIN osm_pt.osm_route_master ol ON ol.osm_route_master_id = match.osm_route_master_id
      LEFT JOIN netex.netex_line nl ON nl.id = match.netex_line_id
      LEFT JOIN osm_pt.osm_pt_network osmn ON ol.network = osmn.network_name;
    -- Add Netex lines that have no match
    INSERT INTO all_lines(netex_line_id, osm_line_id, network, transport_mode, country_code, line_ref, product_category)
    SELECT line.id, NULL, line.network, line.transport_mode, 'NL', line.public_code, line.product_category
    FROM netex.netex_line line
    WHERE true
        AND line.id NOT IN (SELECT netex_line_id FROM all_lines)
        AND (line.product_category NOT IN ('Opstapper', 'OV op Maat') OR line.product_category IS NULL);
    -- Add OSM lines that have no match
    INSERT INTO all_lines(netex_line_id, osm_line_id, network, transport_mode, country_code, line_ref, product_category)
    SELECT NULL, line.osm_route_master_id, line.network, line.transport_mode, network.country_code, line.route_ref, NULL
    FROM osm_pt.osm_route_master line
    LEFT JOIN osm_pt.osm_pt_network network ON network.network_name = line.network 
    LEFT JOIN all_lines ON line.osm_route_master_id = all_lines.osm_line_id
    WHERE all_lines.osm_line_id IS NULL
        AND (network.country_code = 'NL' OR network.country_code IS NULL);
""";

    private static String update_all_routes_table_sql = """
-- Clear the table
TRUNCATE TABLE all_routes;
-- Add routes with an exact quay match between Netex and OSM
INSERT INTO all_routes (line_id, osm_route_id, netex_route_id, matching)
SELECT line.id, osm.osm_route_id, ntx.id AS netex_route_id,
   'Quays match' AS matching
FROM osm_pt.osm_route_data osm
JOIN netex.netex_unique_route ntx ON ntx.line_number = osm.line_number
  AND ntx.quay_list = osm.quay_list
LEFT JOIN all_lines line ON line.osm_line_id = osm.osm_line_id
WHERE osm.osm_route_id NOT IN (SELECT osm_route_id FROM osm_pt.osm_duplicate_bus_route);
-- Add routes with an exact stop_place match between Netex and OSM
INSERT INTO all_routes (line_id, osm_route_id, netex_route_id, matching)
SELECT line.id, osm.osm_route_id, ntx.id AS netex_route_id,
   'Stopplaces match' AS matching
FROM osm_pt.osm_route_data osm
JOIN netex.netex_unique_route ntx ON ntx.line_number = osm.line_number
  AND ntx.stop_place_list = osm.stop_place_list
LEFT JOIN all_lines line ON line.osm_line_id = osm.osm_line_id
WHERE osm.osm_route_id NOT IN (SELECT osm_route_id FROM osm_pt.osm_duplicate_bus_route)
  AND osm.osm_route_id NOT IN (SELECT osm_route_id FROM all_routes)
  AND ntx.id NOT IN (SELECT netex_route_id FROM all_routes);
-- Add routes with matching start- and end stop_places an matching quay count
INSERT INTO all_routes (line_id, osm_route_id, netex_route_id, matching)
SELECT line.id, osm.osm_route_id, ntx.id AS netex_route_id,
   'Endpoints and quay count match' AS matching
FROM osm_pt.osm_route_data osm
JOIN netex.netex_unique_route ntx ON ntx.line_number = osm.line_number
  AND ntx.start_stop_place_code = osm.start_stop_place_code
  AND ntx.end_stop_place_code = osm.end_stop_place_code
  AND ntx.quay_count = osm.quay_count
LEFT JOIN all_lines line ON line.osm_line_id = osm.osm_line_id
WHERE osm.osm_route_id NOT IN (SELECT osm_route_id FROM osm_pt.osm_duplicate_bus_route)
  AND osm.osm_route_id NOT IN (SELECT osm_route_id FROM all_routes)
  AND ntx.id NOT IN (SELECT netex_route_id FROM all_routes);
-- Add routes with matching start- and end stop_places
INSERT INTO all_routes (line_id, osm_route_id, netex_route_id, matching)
SELECT line.id, osm.osm_route_id, ntx.id AS netex_route_id,
   'Endpoints match' AS matching
FROM osm_pt.osm_route_data osm
JOIN netex.netex_unique_route ntx ON ntx.line_number = osm.line_number
  AND ntx.start_stop_place_code = osm.start_stop_place_code
  AND ntx.end_stop_place_code = osm.end_stop_place_code
LEFT JOIN all_lines line ON line.osm_line_id = osm.osm_line_id
WHERE osm.osm_route_id NOT IN (SELECT osm_route_id FROM osm_pt.osm_duplicate_bus_route)
  AND osm.osm_route_id NOT IN (SELECT osm_route_id FROM all_routes)
  AND ntx.id NOT IN (SELECT netex_route_id FROM all_routes);
""";
    
    private static String update_table_osm_missing_quay_code = """
-- Compare osm and netex link to find quay codes for osm quays with missing quay codes
TRUNCATE TABLE osm_pt.osm_missing_quay_code;
WITH candidates AS (
  SELECT ol.osm_quay1_id AS quay_id, ol.osm_primitive_type1 AS osm_primitive_type, nl.quay_code1 AS quay_code
  FROM osm_pt.osm_link ol
  JOIN netex.netex_link nl
  ON ol.stop_place_code1 = nl.stop_place_code1 AND ol.stop_place_code2 = nl.stop_place_code2
  WHERE ol.quay_code1 IS NULL
  UNION
  SELECT ol.osm_quay2_id, ol.osm_primitive_type2 AS osm_primitive_type, nl.quay_code2
  FROM osm_pt.osm_link ol
  JOIN netex.netex_link nl
  ON ol.stop_place_code1 = nl.stop_place_code1 AND ol.stop_place_code2 = nl.stop_place_code2
  WHERE ol.quay_code2 IS NULL
)
-- Restrict to candidates having 1 match
INSERT INTO osm_pt.osm_missing_quay_code
SELECT *
FROM candidates
  WHERE quay_id IN (
  SELECT quay_id
  FROM candidates
  GROUP BY quay_id
  HAVING count(*) = 1)
ORDER BY quay_code;        
""";

    private final EntityManagerFactory entityManagerFactory;

    @Inject
    private ApplicationContext applicationContext;

    @Bean
    EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    /**
     * Defines the main batch job for updating the comparison ETL.
     *
     * @param jobRepository the repository for storing job metadata.
     * @return a configured Job for importing contacts.
     */
    @Bean
    Job updateComparisonEtlJob(JobRepository jobRepository) { 
        return new JobBuilder("comparisonEtlUpdate", jobRepository)
            .start(sqlUpdateStep("Update line match",update_osm_netex_line_match_table_sql))
            .next(sqlUpdateStep("Update all_lines", update_all_lines_table_sql))
            .next(sqlUpdateStep("Update all_routes", update_all_routes_table_sql))
            .build();
    }

    /**
     * Defines an SQL update step to update a.
     *
     * @param stepName The name of the step
     * @param sql The sql code to execute.
     * @return a configured Step.
     */
    Step sqlUpdateStep(String stepName, String sql) {
        var jobRepository = applicationContext.getBean(JobRepository.class);
        var transactionManager = applicationContext.getBean(PlatformTransactionManager.class);
        var transactionTemplate = applicationContext.getBean(TransactionTemplate.class);
        Tasklet tasklet = sqlTasklet(transactionTemplate, sql);
        return new StepBuilder(stepName, jobRepository)
        .tasklet(tasklet, transactionManager)
        .allowStartIfComplete(true)
        .build();
    }

    @SuppressWarnings("static-method")
    @Bean 
    TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);       
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