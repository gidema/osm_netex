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
public class BatchOsmEtlUpdate {

    private static String update_route_master_table_sql = """
TRUNCATE TABLE osm_pt.osm_route_master;
INSERT INTO osm_pt.osm_route_master
SELECT rtm.id AS osm_route_master_id,
  CAST((rtm.tags->'route_master') AS CHARACTER VARYING) AS transport_mode,
  CAST((rtm.tags->'name') AS CHARACTER VARYING) AS "name",
  CAST((rtm.tags->'operator') AS CHARACTER VARYING) AS operator,
  CAST((rtm.tags->'ref') AS CHARACTER VARYING) AS route_ref,
  CAST((rtm.tags->'network') AS CHARACTER VARYING) AS network,
  CAST((rtm.tags->'colour') AS CHARACTER VARYING) AS colour
FROM relations rtm
WHERE rtm.tags->'type' = 'route_master' AND rtm.tags->'route_master'  IN ('bus', 'trolleybus', 'tram', 'train');
""";

    private static String update_route_master_route_table_sql = """
TRUNCATE TABLE osm_pt.osm_route_master_route;
INSERT INTO osm_pt.osm_route_master_route
SELECT master.osm_route_master_id, mb.member_id AS osm_route_id
    FROM relation_members mb
      JOIN osm_pt.osm_route_master master ON master.osm_route_master_id = mb.relation_id;
""";

private static String update_route_table_sql = """
TRUNCATE TABLE osm_pt.osm_route;
INSERT INTO osm_pt.osm_route
SELECT rt.id AS osm_route_id,
  CAST((rt.tags->'route') AS CHARACTER VARYING) AS transport_mode,
  CAST((rt.tags->'name') AS CHARACTER VARYING) AS "name",
  CAST((rt.tags->'operator') AS CHARACTER VARYING) AS operator,
  CAST((rt.tags->'ref') AS CHARACTER VARYING) AS route_ref,
  CAST((rt.tags->'network') AS CHARACTER VARYING) AS network,
  CAST((rt.tags->'from') AS CHARACTER VARYING) AS from,
  CAST((rt.tags->'to') AS CHARACTER VARYING) AS to,
  CAST((rt.tags->'colour') AS CHARACTER VARYING) AS colour,
  rmr.osm_route_master_id AS osm_line_id
FROM relations rt
LEFT JOIN osm_pt.osm_route_master_route rmr ON rmr.osm_route_id = rt.id
WHERE rt.tags->'type' = 'route' AND rt.tags->'route'  IN ('bus', 'trolleybus', 'tram', 'train');
""";

    private static String update_quays_table_sql = """
-- Drop the old table
TRUNCATE TABLE osm_pt.osm_quay;
WITH quay_data AS (
  SELECT n1.id AS osm_id,
    'N' AS osm_primitive_type,
    n1.tags,
    n1.geom AS wgs_location
  FROM nodes n1
    WHERE n1.tags->'public_transport' LIKE 'platform'
UNION
  SELECT w.id AS osm_id,
    'W' AS osm_primitive_type,
    w.tags,
    ST_CENTROID(ST_COLLECT(n2.geom)) AS wgs_location
  FROM ways w
    JOIN way_nodes wn ON wn.way_id = w.id
    JOIN nodes n2 ON n2.id = wn.node_id
  WHERE w.tags->'public_transport' = 'platform'
    AND (w.tags->'railway' IS NULL OR w.tags->'railway' = 'no')
  GROUP BY w.id)
INSERT INTO osm_pt.osm_quay
SELECT osm_id,
  osm_primitive_type,
  CAST((tags->'public_transport') AS character varying) AS platform_type,
  CAST((tags->'name') AS character varying) AS "name",
  CAST((tags->'bus') AS character varying) AS is_bus,
  CAST((tags->'ref') AS character varying) AS stop_side_code,
  CAST((tags->'ref:IFOPT') AS character varying) AS ref_ifopt,
  CAST((tags->'note') AS character varying) AS note,
  wgs_location,
  ST_TRANSFORM(wgs_location, 28992) AS rd_location
FROM quay_data;
""";
  
    private static String update_route_quays_table_sql = """
TRUNCATE TABLE osm_pt.osm_route_quay;
INSERT INTO osm_pt.osm_route_quay (osm_route_id, osm_quay_id, osm_primitive_type,
  quay_name, quay_index, quay_code, stop_side_code, stop_place_code, quay_location_type,
  entry_only, exit_only)
SELECT sub.osm_route_id, 
  sub.osm_quay_id,
  sub.osm_primitive_type,
  sub.quay_name,
  sub.quay_index,
  sub.quay_code,
  sub.stop_side_code,
  sub.stop_place_code,
  CASE WHEN sub.quay_index=1 THEN 'start' WHEN sub.quay_index = sub.count THEN 'end' ELSE 'middle' END AS quay_location_type,
  sub.entry_only,
  sub.exit_only
FROM (
  SELECT mb.relation_id AS osm_route_id,
    mb.member_id AS osm_quay_id,
    mb.member_type AS osm_primitive_type,
    osm_quay.name AS quay_name,
    osm_quay.ref_ifopt AS quay_code,
    osm_quay.stop_side_code,
    csp.stop_place_code AS stop_place_code,
    ROW_NUMBER() OVER (
      PARTITION BY mb.relation_id
      ORDER BY mb.sequence_id ASC
    ) AS quay_index,
    COUNT(*) OVER (
      PARTITION BY mb.relation_id
    ) AS count,
    mb.member_role = 'platform_entry_only' AS entry_only,
    mb.member_role = 'platform_exit_only' AS exit_only
  FROM relation_members mb
  JOIN osm_pt.osm_route route ON route.osm_route_id = mb.relation_id AND mb.member_role LIKE 'platform%'
  JOIN osm_pt.osm_pt_network nw ON route.network = nw.network_name
  LEFT JOIN osm_pt.osm_quay ON osm_quay.osm_id = mb.member_id AND osm_quay.osm_primitive_type = mb.member_type
  LEFT JOIN chb.chb_quay chb_quay ON chb_quay.quay_code = osm_quay.ref_ifopt
  LEFT JOIN chb.chb_stop_place csp ON csp.id = chb_quay.stop_place_id
  WHERE nw.country_code = 'NL' AND route.transport_mode LIKE '%bus'
) AS sub;
""";

    private static String update_route_quay2_table_sql = """
TRUNCATE TABLE osm_pt.osm_route_quay2;
INSERT INTO osm_pt.osm_route_quay2 (osm_route_id, quay_index, osm_quay_node_id, osm_quay_way_id, quay_code,
    stop_side_code_n, stop_side_code_w, area_code)
SELECT osm_route_id,
  ROW_NUMBER() OVER (
      PARTITION BY osm_route_id
      ORDER BY quay_index ASC
    ) AS quay_index,
    osm_quay_node_id,
    osm_quay_way_id,
    quay_code,
    stop_side_code_n,
    stop_side_code_w,
    stop_place_code AS area_code
FROM (
SELECT COALESCE(rqn.osm_route_id, rqw.osm_route_id) AS osm_route_id,
  LEAST(rqn.quay_index, rqw.quay_index) AS quay_index,
  rqn.osm_quay_id AS osm_quay_node_id, 
  rqw.osm_quay_id AS osm_quay_way_id, 
  COALESCE(rqn.quay_code, rqw.quay_code) AS quay_code,
  rqn.stop_side_code AS stop_side_code_n,
  rqn.stop_side_code AS stop_side_code_w,
  COALESCE(rqn.stop_place_code, rqw.stop_place_code) AS stop_place_code
FROM osm_pt.osm_route_quay rqn
FULL OUTER JOIN osm_pt.osm_route_quay rqw 
  ON rqn.osm_route_id = rqw.osm_route_id 
  AND ABS(rqn.quay_index - rqw.quay_index) = 1
  AND ((rqn.quay_code = rqw.quay_code AND rqn.quay_code IS NOT NULL)
    OR (rqn.quay_name = rqw.quay_name AND (rqn.quay_code IS NULL OR rqw.quay_code IS NULL)))
WHERE TRUE
  AND (rqn.osm_primitive_type = 'N' OR rqn.osm_primitive_type IS NULL)
  AND (rqw.osm_primitive_type = 'W' OR rqw.osm_primitive_type IS NULL)
) AS sub;
""";
    
    private static String update_osm_route_data_table_sql_bak = """
TRUNCATE TABLE osm_pt.osm_route_data;
WITH data AS (
  SELECT route.route_ref AS line_number,
    route.osm_route_id,
    route.network AS network,
    ARRAY_AGG(route_quay.quay_code ORDER BY route_quay.quay_index) quay_list,
    ARRAY_AGG(route_quay.stop_place_code ORDER BY route_quay.quay_index) stop_place_list,
    CAST(COUNT(route_quay.quay_code) AS INTEGER) AS quay_count,
    route.osm_line_id
  FROM osm_pt.osm_route AS route
  JOIN osm_pt.osm_route_quay AS route_quay ON route_quay.osm_route_id = route.osm_route_id
  GROUP BY route.route_ref, route.osm_route_id, route.osm_line_id, route.network)
INSERT INTO osm_pt.osm_route_data
SELECT data.line_number,
    data.osm_route_id,
    data.network AS network,
    data.quay_list,
    data.stop_place_list,
    data.quay_count,
    start_quay.quay_code AS start_quay_code,
    end_quay.quay_code AS end_quay_code,
    start_quay.stop_place_code AS start_stop_place_code,
    end_quay.stop_place_code AS end_stop_place_code,
    data.osm_line_id
FROM data
JOIN osm_pt.osm_route_quay start_quay ON start_quay.osm_route_id = data.osm_route_id AND start_quay.quay_location_type = 'start'
JOIN osm_pt.osm_route_quay end_quay ON end_quay.osm_route_id = data.osm_route_id AND end_quay.quay_location_type = 'end';
""";
    
    private static String update_osm_route_data_table_sql = """
    TRUNCATE TABLE osm_pt.osm_route_data;
    WITH data AS (
        SELECT route.route_ref AS line_number,
          route.osm_route_id,
          route.network AS network,
          ARRAY_AGG(route_quay.quay_code ORDER BY route_quay.quay_index) quay_list,
          ARRAY_AGG(route_quay.area_code ORDER BY route_quay.quay_index) stop_place_list,
          CAST(COUNT(route_quay.quay_code) AS INTEGER) AS quay_count,
          route.osm_line_id
        FROM osm_pt.osm_route AS route
        JOIN osm_pt.osm_route_quay2 AS route_quay ON route_quay.osm_route_id = route.osm_route_id
        GROUP BY route.route_ref, route.osm_route_id, route.osm_line_id, route.network)
    INSERT INTO osm_pt.osm_route_data
          SELECT data.line_number,
              data.osm_route_id,
              data.network AS network,
              data.quay_list,
              data.stop_place_list,
              data.quay_count,
              start_quay.quay_code AS start_quay_code,
              end_quay.quay_code AS end_quay_code,
              start_quay.stop_place_code AS start_stop_place_code,
              end_quay.stop_place_code AS end_stop_place_code,
              data.osm_line_id
          FROM data
          JOIN osm_pt.osm_route_quay start_quay ON start_quay.osm_route_id = data.osm_route_id AND start_quay.quay_location_type = 'start'
          JOIN osm_pt.osm_route_quay end_quay ON end_quay.osm_route_id = data.osm_route_id AND end_quay.quay_location_type = 'end';
""";


    private static String update_osm_route_master_endpoint_table_sql = """
TRUNCATE TABLE osm_pt.osm_route_master_endpoint;
INSERT INTO osm_pt.osm_route_master_endpoint
SELECT DISTINCT *
    FROM (
      SELECT master.osm_route_master_id, rd.line_number, rd.start_stop_place_code
      FROM osm_pt.osm_route_master_route rmr
        JOIN osm_pt.osm_route_master master ON master.osm_route_master_id = rmr.osm_route_master_id
        JOIN osm_pt.osm_route_data rd ON rd.osm_route_id = rmr.osm_route_id
      WHERE rd.start_stop_place_code IS NOT NULL
      UNION
      SELECT master.osm_route_master_id, rd.line_number, rd.end_stop_place_code
      FROM osm_pt.osm_route_master_route rmr
        JOIN osm_pt.osm_route_master master ON master.osm_route_master_id = rmr.osm_route_master_id
        JOIN osm_pt.osm_route_data rd ON rd.osm_route_id = rmr.osm_route_id
      WHERE rd.end_stop_place_code IS NOT NULL
    ) AS SUB;
""";

    private static String update_osm_duplicate_bus_route_table_sql = """
TRUNCATE TABLE osm_pt.osm_duplicate_bus_route;
WITH duplicates AS (
  SELECT rd.network, rd.line_number, quay_list
  FROM osm_pt.osm_route_data rd
  JOIN osm_pt.osm_route rt ON rd.osm_route_id = rt.osm_route_id
  JOIN osm_pt.osm_pt_network nw ON nw.network_name = rd.network
  WHERE rt.transport_mode IN('bus','trolleybus') AND nw.country_code = 'NL'
  GROUP BY rd.network, rd.line_number, quay_list
  HAVING count(rd.osm_route_id) > 1)
INSERT INTO osm_pt.osm_duplicate_bus_route
SElECT DISTINCT rd.osm_route_id
FROM osm_pt.osm_route_data rd
JOIN duplicates dup ON rd.line_number = dup.line_number AND rd.quay_list = dup.quay_list
""";

    private static String update_missing_ifopt_area_code_table_sql = """
-- For osm quays without a quay_code (ref:IFOPT), find the stoparea_code, from the chb data based on the distance to
-- the stop area centroid and a matching name between the quay and the stop_area 
TRUNCATE TABLE osm_pt.osm_missing_ifopt_area_code;
INSERT INTO osm_pt.osm_missing_ifopt_area_code
SELECT osmq.osm_id, osmq.osm_primitive_type, osmq.name AS osm_name, osmq.stop_side_code, sa.name AS netex_name, ST_DISTANCE(sa.rd_centroid, osmq.rd_location) AS distance, sa.area_code
FROM osm_pt.osm_quay osmq, chb.chb_stop_area sa
WHERE ST_DISTANCE(sa.rd_centroid, osmq.rd_location) < 100
  AND osmq.ref_ifopt IS NULL
  AND osmq.osm_id IN (
    SELECT DISTINCT rq.osm_quay_node_id
    FROM osm_pt.osm_route_quay2 rq
    JOIN osm_pt.osm_route r ON rq.osm_route_id = r.osm_route_id
  );
""";
    
    private static String update_missing_ifopt_area_code_table_sql_bak = """
-- For osm quays without a quay_code (ref:IFOPT), find the stoparea_code, from the chb data based on the distance to
-- the stop area centroid and a matching name between the quay and the stop_area 
TRUNCATE TABLE osm_pt.osm_missing_ifopt_area_code;
INSERT INTO osm_pt.osm_missing_ifopt_area_code
SELECT osmq.osm_id, osmq.osm_primitive_type, osmq.name AS osm_name, osmq.stop_side_code, sa.name AS netex_name, ST_DISTANCE(sa.rd_centroid, osmq.rd_location) AS distance, sa.area_code
FROM osm_pt.osm_quay osmq, chb.chb_stop_area sa
WHERE ST_DISTANCE(sa.rd_centroid, osmq.rd_location) < 100
  AND osmq.ref_ifopt IS NULL
  AND osmq.osm_id IN (
    SELECT DISTINCT rq.osm_quay_id
    FROM osm_pt.osm_route_quay2 rq
    JOIN osm_pt.osm_route r ON rq.osm_route_id = r.osm_route_id
  );
""";
    
    private static String update_osm_link_table_sql = """
-- Create a table of unique links between 2 osm quays
-- Lookup missing area_codes in the osm_missing_ifopt_area_code table
TRUNCATE TABLE osm_pt.osm_link;
INSERT INTO osm_pt.osm_link
SELECT DISTINCT rq1.osm_quay_id AS osm_quay1_id, rq1.osm_primitive_type AS osm_primitive_type1, rq1.quay_code AS quay_code1, rq1.stop_side_code AS stop_side_code1, COALESCE(rq1.stop_place_code, mac1.area_code) AS stop_place_code1,
    rq2.osm_quay_id AS osm_quay2_id, rq2.osm_primitive_type AS osm_primitive_type2, rq2.quay_code AS quay_code2, rq2.stop_side_code AS stop_side_code2, COALESCE(rq2.stop_place_code, mac2.area_code) AS stop_place_code2
FROM osm_pt.osm_route_quay rq1
JOIN osm_pt.osm_route_quay rq2 ON rq1.osm_route_id = rq2.osm_route_id AND rq2.quay_index = rq1.quay_index + 1
LEFT JOIN osm_pt.osm_missing_ifopt_area_code mac1 ON mac1.osm_id = rq1.osm_quay_id
LEFT JOIN osm_pt.osm_missing_ifopt_area_code mac2 ON mac2.osm_id = rq2.osm_quay_id           
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
    @Bean
    Job updateOsmEtlJob(JobRepository jobRepository) { 
        return new JobBuilder("osmEtlUpdate", jobRepository)
            .start(sqlUpdateStep("Update routemasters",update_route_master_table_sql))
            .next(sqlUpdateStep("Update routes", update_route_table_sql))
            .next(sqlUpdateStep("Update routemaster routes", update_route_master_route_table_sql))
            .next(sqlUpdateStep("Update quays", update_quays_table_sql))
            .next(sqlUpdateStep("Update route quays", update_route_quays_table_sql))
            .next(sqlUpdateStep("Update route quays2", update_route_quay2_table_sql))
            .next(sqlUpdateStep("Update route data", update_osm_route_data_table_sql))
            .next(sqlUpdateStep("Update routemaster endpoints", update_osm_route_master_endpoint_table_sql))
            .next(sqlUpdateStep("Update duplicate routes", update_osm_duplicate_bus_route_table_sql))
            .next(sqlUpdateStep("Update missing ifopt areacodes", update_missing_ifopt_area_code_table_sql))
            .next(sqlUpdateStep("update osm links", update_osm_link_table_sql))
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