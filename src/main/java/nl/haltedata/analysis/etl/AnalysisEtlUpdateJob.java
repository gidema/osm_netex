package nl.haltedata.analysis.etl;

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
public class AnalysisEtlUpdateJob {

    private static String check_route_in_multiple_lines = """
DELETE FROM route_issues WHERE "message" = 'RouteInMultipleLines';
INSERT INTO route_issues (entity_id, "sequence", "message", parameters)
SELECT route_id, 1 AS "sequence", 'RouteInMultipleLines', ARRAY["count"] AS parameters
FROM osm_pt.issue_route_in_multiple_lines;
""";
    
    private static String update_network_match_table_sql = """
-- Clear the table
TRUNCATE TABLE network_match;
WITH netex AS (
  SELECT "id", administrative_zone, "name", short_name
  FROM netex.netex_network
  WHERE to_date IS NULL OR short_name IN ('FFVB', 'RAIL-HRN')),
osm AS (
  SELECT "id", "name", short_name, administrative_zone
  FROM osm_pt.osm_network
  WHERE is_concessie
)
INSERT INTO network_match (id, administrative_zone, name, short_name, osm_id, netex_id)
SELECT COALESCE(netex."id", osm.id::TEXT) AS netex_id, netex.administrative_zone, COALESCE(netex."name", osm."name") AS "name",
  COALESCE(netex.short_name, osm.short_name) AS short_name, 
  osm.id AS osm_id,
  netex."id"
FROM netex
FULL OUTER JOIN osm ON osm.administrative_zone = netex.administrative_zone
""";
    
    private static String update_line_match_table_sql = """
-- Clear the table
TRUNCATE TABLE line_match;
WITH match AS (
  SELECT DISTINCT olsp.administrative_zone, olsp.line_number, olsp.line_id AS osm_line_id, nlsp.netex_line_id
  FROM osm_pt.osm_line_stop_place olsp
  JOIN netex.netex_line_stop_place nlsp ON nlsp.line_number = olsp.line_number 
    AND nlsp.stop_place_code = olsp.stop_place_code AND nlsp.administrative_zone = olsp.administrative_zone)
INSERT INTO line_match(netex_line_id, osm_line_id, transport_mode, line_number, product_category, line_sort, administrative_zone)
SELECT match.netex_line_id, match.osm_line_id, nl.transport_mode, match.line_number, nl.product_category,
ol.line_sort, match.administrative_zone
FROM match 
  LEFT JOIN osm_pt.osm_line ol ON ol.id = match.osm_line_id
  LEFT JOIN netex.netex_line nl ON nl.id = match.netex_line_id;
-- Add Netex lines that have no match
/*INSERT INTO line_match(netex_line_id, transport_mode, line_number, product_category, line_sort, administrative_zone)
SELECT line.id, line.transport_mode, line.public_code, line.product_category, line.line_sort, line.administrative_zone
FROM netex.netex_line line
WHERE line.id NOT IN (SELECT netex_line_id FROM line_match)
    AND (line.product_category NOT IN ('Opstapper', 'OV op Maat') OR line.product_category IS NULL);
-- Add OSM lines that have no match
INSERT INTO line_match(osm_line_id, transport_mode, line_number, administrative_zone, line_sort)
SELECT line.id, line.netex_transport_mode, line.line_number, nw.administrative_zone, line.line_sort
FROM osm_pt.osm_line line
LEFT JOIN line_match ON line.id = line_match.osm_line_id
JOIN osm_pt.osm_line_in_network olin ON olin.line_id = line.id
JOIN osm_pt.osm_network nw ON nw.id = olin.network_id
WHERE line_match.osm_line_id IS NULL*/;
""";

    private static String update_route_match_candidate_table_sql = """
    -- Clear the table
    TRUNCATE TABLE route_match_candidate;
    WITH osm_links AS (
      SELECT DISTINCT lm.id AS line_id, rt.route_ref AS line_number, rq1.osm_route_id, rq1.stop_place AS stop_place1, rq2.stop_place AS stop_place2
      FROM osm_pt.osm_route_quay rq1
      JOIN osm_pt.osm_route_quay rq2 ON rq1.osm_route_id = rq2.osm_route_id AND rq2.quay_index = rq1.quay_index + 1
      JOIN osm_pt.osm_route rt ON rt.osm_route_id = rq1.osm_route_id
      LEFT JOIN line_match lm ON lm.osm_line_id = rt.osm_line_id
      WHERE rq1.stop_place IS NOT NULL AND rq2.stop_place IS NOT NULL),
    netex_links AS (
    SELECT DISTINCT lm.id AS line_id, rq1.line_number, rq1.variant_id, rq1.stop_place_code AS stop_place1, rq2.stop_place_code AS stop_place2
      FROM netex.netex_route_variant_quay rq1
      JOIN netex.netex_route_variant_quay rq2 ON rq1.variant_id = rq2.variant_id AND rq2.quay_index = rq1.quay_index + 1
      JOIN netex.netex_route_variant rv ON rv.id = rq1.variant_id
      LEFT JOIN line_match lm ON lm.netex_line_id = rv.line_ref
      WHERE rq1.stop_place_code IS NOT NULL AND rq2.stop_place_code IS NOT NULL),
    matches AS (
      SELECT COALESCE(osm.line_id, ntx.line_id) AS line_id, osm.osm_route_id, ntx.variant_id, count(*) AS link_count
      FROM osm_links osm
      JOIN netex_links ntx ON osm.line_number = ntx.line_number AND osm.stop_place1 = ntx.stop_place1 AND osm.stop_place2 = ntx.stop_place2
      GROUP BY COALESCE(osm.line_id, ntx.line_id), osm.osm_route_id, ntx.variant_id)
    INSERT INTO route_match_candidate(line_id, osm_route_id, variant_id, link_count, match_rate, matching, administrative_zone)
    SELECT matches.line_id, matches.osm_route_id, matches.variant_id, matches.link_count, 
      LEAST(matches.link_count / (nrv.quay_count - 1.0), matches.link_count / (ord.quay_count - 1.0)) * 100 AS match_rate,
      CASE WHEN nrv.quay_list = ord.quay_list THEN 'Exact Quay match'
        WHEN nrv.stop_place_list = ord.stop_place_list THEN 'Exact Stopplace match'
        WHEN matches.osm_route_id IS NOT NULL AND matches.variant_id IS NOT NULL THEN 'Best Candidate match'
        ELSE 'No match'
      END AS matching,
      nrv.administrative_zone
    FROM matches
    JOIN netex.netex_route_variant nrv ON nrv.id = matches.variant_id
    JOIN osm_pt.osm_route_data ord ON ord.osm_route_id = matches.osm_route_id
    LEFT JOIN osm_pt.osm_line ol ON ol.id = ord.osm_line_id
    ORDER BY osm_route_id, match_rate DESC
""";
    
  private static String update_route_match_table_sql = """
-- Clear the table
TRUNCATE TABLE route_match;
-- Get the best matches from the route match candidates
INSERT INTO route_match (line_id, osm_route_id, matching, variant_id, link_count, match_rate, administrative_zone)
SELECT sub.line_id, sub.osm_route_id, sub.matching, sub.variant_id, sub.link_count, sub.match_rate, sub.administrative_zone
FROM (
  SELECT osm_route_id, variant_id, link_count, match_rate, line_id, matching,
          ROW_NUMBER() OVER (
          PARTITION BY osm_route_id
          ORDER BY match_rate DESC
        ) AS "rank1",
          ROW_NUMBER() OVER (
          PARTITION BY variant_id
          ORDER BY match_rate DESC
        ) AS "rank2",
        COUNT(*) OVER (
          PARTITION BY osm_route_id
        ) AS candidate_count,
        administrative_zone
  FROM route_match_candidate) AS sub
JOIN osm_pt.osm_route ort ON ort.osm_route_id = sub.osm_route_id
WHERE sub.rank1 = 1 AND sub.rank2 = 1;
-- Add the un-matched OSM routes
/*INSERT INTO route_match (line_id, osm_route_id, matching, variant_id, link_count, match_rate)
SELECT NULL, osm_route_id, 'No Netex match for OSM route', null, 0, 0.0
FROM osm_pt.osm_route
WHERE osm_route_id NOT IN (SELECT osm_route_id FROM route_match)*/;
""";
  
  private static String update_route_validation_table_sql = """
-- Clear the table
TRUNCATE TABLE route_validation;
-- Get the best matches from the route match candidates
-- INSERT INTO route_validation (line_id, osm_route_id, matching, variant_id, link_count, match_rate)
INSERT INTO route_validation
SELECT sub.line_id, sub.osm_route_id, sub.matching, sub.variant_id, sub.link_count, sub.match_rate
FROM (
  SELECT osm_route_id, variant_id, link_count, match_rate, line_id, matching,
          ROW_NUMBER() OVER (
          PARTITION BY osm_route_id
          ORDER BY match_rate DESC
        ) AS "rank1",
          ROW_NUMBER() OVER (
          PARTITION BY variant_id
          ORDER BY match_rate DESC
        ) AS "rank2",
        COUNT(*) OVER (
          PARTITION BY osm_route_id
        ) AS candidate_count
  FROM route_match_candidate) AS sub
JOIN osm_pt.osm_route ort ON ort.osm_route_id = sub.osm_route_id
WHERE sub.rank1 = 1 AND sub.rank2 = 1;
-- Add the un-matched OSM routes
INSERT INTO route_match (line_id, osm_route_id, matching, variant_id, link_count, match_rate)
SELECT NULL, osm_route_id, 'No Netex match for OSM route', null, 0, 0.0
FROM osm_pt.osm_route
WHERE osm_route_id NOT IN (SELECT osm_route_id FROM route_match);
""";
  
//    private static String update_route_match_table_sql = """;
//          
//-- Clear the table
//TRUNCATE TABLE route_match;
//-- Add routes with an exact quay match between Netex and OSM
//INSERT INTO route_match (line_id, osm_route_id, variant_id, matching)
//SELECT line.id, osm.osm_route_id, ntx.id AS netex_route_id,
//   'Quays match' AS matching
//FROM osm_pt.osm_route_data osm
//JOIN netex.netex_route_variant ntx ON ntx.line_number = osm.line_number
//  AND ntx.quay_list = osm.quay_list
//LEFT JOIN line_match line ON line.osm_line_id = osm.osm_line_id
//WHERE osm.osm_route_id NOT IN (SELECT osm_route_id FROM osm_pt.osm_duplicate_bus_route);
//
//-- Add routes with an exact stop_place match between Netex and OSM
//INSERT INTO route_match (line_id, osm_route_id, netex_route_id, matching)
//SELECT line.id, osm.osm_route_id, ntx.id AS netex_route_id,
//   'Stopplaces match' AS matching
//FROM osm_pt.osm_route_data osm
//JOIN netex.netex_route_variant ntx ON ntx.line_number = osm.line_number
//  AND ntx.stop_place_list = osm.stop_place_list
//LEFT JOIN line_match line ON line.osm_line_id = osm.osm_line_id
//WHERE osm.osm_route_id NOT IN (SELECT osm_route_id FROM osm_pt.osm_duplicate_bus_route)
//  AND osm.osm_route_id NOT IN (SELECT osm_route_id FROM route_match)
//  AND ntx.id NOT IN (SELECT netex_route_id FROM route_match);
//
//-- Add routes with matching start- and end stop_places an matching quay count
//INSERT INTO route_match (line_id, osm_route_id, netex_route_id, matching)
//SELECT line.id, osm.osm_route_id, ntx.id AS netex_route_id,
//   'Endpoints and quay count match' AS matching
//FROM osm_pt.osm_route_data osm
//JOIN netex.netex_route_variant ntx ON ntx.line_number = osm.line_number
//  AND ntx.start_stop_place_code = osm.start_stop_place_code
//  AND ntx.end_stop_place_code = osm.end_stop_place_code
//  AND ntx.quay_count = osm.quay_count
//LEFT JOIN line_match line ON line.osm_line_id = osm.osm_line_id
//WHERE osm.osm_route_id NOT IN (SELECT osm_route_id FROM osm_pt.osm_duplicate_bus_route)
//  AND osm.osm_route_id NOT IN (SELECT osm_route_id FROM route_match)
//  AND ntx.id NOT IN (SELECT netex_route_id FROM route_match);
//
//-- Add routes with matching start- and end stop_places
//INSERT INTO route_match (line_id, osm_route_id, netex_route_id, matching)
//SELECT line.id, osm.osm_route_id, ntx.id AS netex_route_id,
//   'Endpoints match' AS matching
//FROM osm_pt.osm_route_data osm
//JOIN netex.netex_route_variant ntx ON ntx.line_number = osm.line_number
//  AND ntx.start_stop_place_code = osm.start_stop_place_code
//  AND ntx.end_stop_place_code = osm.end_stop_place_code
//LEFT JOIN line_match line ON line.osm_line_id = osm.osm_line_id
//WHERE osm.osm_route_id NOT IN (SELECT osm_route_id FROM osm_pt.osm_duplicate_bus_route)
//  AND osm.osm_route_id NOT IN (SELECT osm_route_id FROM route_match)
//  AND ntx.id NOT IN (SELECT netex_route_id FROM route_match);
//""";
////-- Add non-matching Netex routes
////INSERT INTO all_routes (line_id, osm_route_id, netex_route_id, matching)
////SELECT line.id, NULL, ntx.id, 'No matching OSM route'
////FROM netex.netex_route_variant ntx
////WHERE ntx.id NOT IN (SELECT netex_route_id FROM all_routes);
////-- Add non-matching OSM routes
////INSERT INTO all_routes (line_id, osm_route_id, netex_route_id, matching)
////SELECT line.id, osm.osm_route_id, NULL, 'No matching Netex route'
////FROM osm_pt.osm_route_data osm
////LEFT JOIN all_lines line ON line.osm_line_id = osm.osm_line_id
////WHERE osm.osm_route_id NOT IN (SELECT osm_route_id FROM osm_pt.osm_duplicate_bus_route)
////  AND osm.osm_route_id NOT IN (SELECT osm_route_id FROM all_routes);
////""";
    
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
  HAVING count(*) = 1);
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
            .start(sqlUpdateStep("Check route in multipe lines", check_route_in_multiple_lines))
            .next(sqlUpdateStep("Update network match", update_network_match_table_sql))
            .next(sqlUpdateStep("Update line match", update_line_match_table_sql))
            .next(sqlUpdateStep("Update line matches", update_route_match_candidate_table_sql))
            .next(sqlUpdateStep("Update route_matches", update_route_match_table_sql))
            .next(sqlUpdateStep("Update osm_missing_quay", update_table_osm_missing_quay_code))
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