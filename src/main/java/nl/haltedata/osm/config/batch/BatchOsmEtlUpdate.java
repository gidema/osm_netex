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

    private static String update_st_network_table_sql = """
TRUNCATE TABLE osm_pt.st_osm_network;
INSERT INTO osm_pt.st_osm_network ("id", "name", short_name, "operator", start_date, end_date,
    wikidata, note)
SELECT network.id AS "id",
  network.tags->'name' AS "name",
  network.tags->'short_name' AS short_name,
  network.tags->'operator' AS "operator",
  network.tags->'start_date' AS start_date,
  network.tags->'end_date' AS end_date,
  network.tags->'wikidata' AS wikidata,
  network.tags->'note' AS note
FROM relations network
WHERE network.tags->'type' = 'network' AND network.tags->'network' = 'public_transport';
""";

    private static String update_network_line_table_sql = """
TRUNCATE TABLE osm_pt.st_osm_network_line;
INSERT INTO osm_pt.st_osm_network_line (network_id, route_master_id)
    SELECT network.id AS network_id, mb.member_id AS route_master_id
    FROM relation_members mb
        JOIN osm_pt.st_osm_network network ON network.id = mb.relation_id
""";
    
    private static String update_route_master_table_sql = """
TRUNCATE TABLE osm_pt.osm_route_master;
INSERT INTO osm_pt.osm_route_master
SELECT rtm.id AS osm_route_master_id,
  COALESCE((rtm.tags->'route_master'), (rtm.tags->'disused:route_master')) AS transport_mode,
  rtm.tags->'name' AS "name",
  rtm.tags->'operator' AS operator,
  rtm.tags->'ref' AS route_ref,
  rtm.tags->'network' AS network,
  rtm.tags->'colour' AS colour,
  CAST(NULLIF(regexp_replace((rtm.tags->'ref'), '\\D','','g'), '') AS INTEGER) AS line_number,
  exist(rtm.tags, 'disused:route_master') AS is_disused 
FROM relations rtm
WHERE rtm.tags->'type' = 'route_master';
""";

    private static String update_route_master_route_table_sql = """
TRUNCATE TABLE osm_pt.osm_route_master_route;
INSERT INTO osm_pt.osm_route_master_route
SELECT master.osm_route_master_id, mb.member_id AS osm_route_id
    FROM relation_members mb
      JOIN osm_pt.osm_route_master master ON master.osm_route_master_id = mb.relation_id;
""";

    private static String update_network_table_sql = """
TRUNCATE TABLE osm_pt.osm_network;
WITH concessie AS ( 
    SELECT nwln.route_master_id AS id
    FROM osm_pt.st_osm_network_line nwln
    JOIN osm_pt.st_osm_network concessie ON nwln.network_id = concessie.id
        WHERE concessie.name = 'OV-concessies Nederland')
INSERT INTO osm_pt.osm_network
SELECT nw.*, concessie.id IS NOT NULL AS is_concessie
FROM osm_pt.st_osm_network nw
  LEFT JOIN concessie ON nw.id = concessie.id
  WHERE nw.name != 'OV-concessies Nederland'
""";
            
    private static String update_route_table_sql = """
TRUNCATE TABLE osm_pt.osm_route;
INSERT INTO osm_pt.osm_route
SELECT rt.id AS osm_route_id,
  COALESCE(rt.tags->'route', rt.tags->'disused:route') AS transport_mode,
  rt.tags->'name' AS "name",
  rt.tags->'operator' AS operator,
  rt.tags->'ref' AS route_ref,
  rt.tags->'network' AS network,
  rt.tags->'from' AS from,
  rt.tags->'to' AS to,
  rt.tags->'colour' AS colour,
  rmr.osm_route_master_id AS osm_line_id,
  CAST(NULLIF(regexp_replace(rt.tags->'ref', '\\D','','g'), '') AS INTEGER) AS line_number,
  exist(rt.tags, 'disused:route') AS is_disused 
FROM relations rt
LEFT JOIN osm_pt.osm_route_master_route rmr ON rmr.osm_route_id = rt.id
WHERE rt.tags->'type' = 'route' AND rt.tags->'route'  IN ('bus', 'trolleybus', 'tram', 'train');
""";

    private static String update_quays_table_sql = """
-- Truncate the table
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
  GROUP BY w.id)
INSERT INTO osm_pt.osm_quay
SELECT osm_id,
  osm_primitive_type,
  tags->'public_transport' AS platform_type,
  tags->'name' AS "name",
  tags->'bus' AS is_bus,
  tags->'ref' AS stop_side_code,
  tags->'ref:IFOPT' AS ref_ifopt,
  tags->'note' AS note,
  wgs_location,
  ST_TRANSFORM(wgs_location, 28992) AS rd_location
FROM quay_data;
""";
  
    private static String update_route_platform_table_sql = """
TRUNCATE TABLE osm_pt.osm_route_platform;
INSERT INTO osm_pt.osm_route_platform (osm_route_id, osm_platform_id, osm_primitive_type,
  quay_name, platform_index, quay_code, stop_side_code, stop_place_code, quay_location_type,
  entry_only, exit_only)
SELECT sub.osm_route_id, 
  sub.osm_platform_id,
  sub.osm_primitive_type,
  sub.quay_name,
  sub.platform_index,
  sub.quay_code,
  sub.stop_side_code,
  sub.stop_place_code,
  CASE WHEN sub.platform_index=1 THEN 'start' WHEN sub.platform_index = sub.count THEN 'end' ELSE 'middle' END AS quay_location_type,
  sub.entry_only,
  sub.exit_only
FROM (
  SELECT mb.relation_id AS osm_route_id,
    mb.member_id AS osm_platform_id,
    mb.member_type AS osm_primitive_type,
    osm_quay.name AS quay_name,
    osm_quay.ref_ifopt AS quay_code,
    osm_quay.stop_side_code,
    csp.stop_place_code AS stop_place_code,
    ROW_NUMBER() OVER (
      PARTITION BY mb.relation_id
      ORDER BY mb.sequence_id ASC
    ) AS platform_index,
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

    private static String update_route_quay_table_sql = """
TRUNCATE TABLE osm_pt.osm_route_quay;
WITH data AS (
SELECT osm_route_id,
  ROW_NUMBER() OVER (
      PARTITION BY osm_route_id
      ORDER BY platform_index ASC
    ) AS quay_index,
  COUNT(*) OVER (
      PARTITION BY osm_route_id
    ) AS quay_count,
    platform_point_id,
    platform_area_id,
    quay_name,
    quay_code,
    stop_side_code,
    area_code
FROM (
SELECT COALESCE(rpn.osm_route_id, rpw.osm_route_id) AS osm_route_id,
  rpn.osm_platform_id AS platform_point_id, 
  rpw.osm_platform_id AS platform_area_id, 
  LEAST(rpn.platform_index, rpw.platform_index) AS platform_index,
  COALESCE(rpn.quay_name, rpw.quay_name) AS quay_name,
  COALESCE(rpn.quay_code, rpw.quay_code) AS quay_code,
  COALESCE(rpn.stop_side_code, rpw.stop_side_code) AS stop_side_code,
  COALESCE(rpn.stop_place_code, rpw.stop_place_code) AS area_code
FROM osm_pt.osm_route_platform rpn
FULL OUTER JOIN osm_pt.osm_route_platform rpw 
  ON rpn.osm_route_id = rpw.osm_route_id 
  AND ABS(rpn.platform_index - rpw.platform_index) = 1
  AND ((rpn.quay_code = rpw.quay_code AND rpn.quay_code IS NOT NULL)
    OR (rpn.quay_name = rpw.quay_name AND (rpn.quay_code IS NULL OR rpw.quay_code IS NULL)))
WHERE TRUE
  AND (rpn.osm_primitive_type = 'N' OR rpn.osm_primitive_type IS NULL)
  AND (rpw.osm_primitive_type = 'W' OR rpw.osm_primitive_type IS NULL)
) AS sub)
INSERT INTO osm_pt.osm_route_quay (osm_route_id, quay_index, platform_point_id, platform_area_id,
 quay_name, quay_code, stop_side_code, area_code, quay_location_type)
SELECT osm_route_id, quay_index, platform_point_id, platform_area_id, quay_name, quay_code,
    stop_side_code, area_code,
    CASE WHEN quay_index=1 THEN 'start' WHEN quay_index = quay_count THEN 'end' ELSE 'middle' END AS quay_location_type
FROM data;
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
              start_quay.area_code AS start_stop_place_code,
              end_quay.area_code AS end_stop_place_code,
              data.osm_line_id
          FROM data
          JOIN osm_pt.osm_route_quay start_quay ON start_quay.osm_route_id = data.osm_route_id AND start_quay.quay_location_type = 'start'
          JOIN osm_pt.osm_route_quay end_quay ON end_quay.osm_route_id = data.osm_route_id AND end_quay.quay_location_type = 'end';
""";


    private static String update_osm_route_master_stop_place_table_sql = """
TRUNCATE TABLE osm_pt.osm_route_master_stop_place;
INSERT INTO osm_pt.osm_route_master_stop_place
SELECT DISTINCT rm.osm_route_master_id, rm.line_number, rq.area_code AS stop_place_code
FROM osm_pt.osm_route_quay rq
  JOIN osm_pt.osm_route_master_route rmr ON rq.osm_route_id = rmr.osm_route_id
  JOIN osm_pt.osm_route_master rm ON rm.osm_route_master_id = rmr.osm_route_master_id
WHERE rq.area_code IS NOT NULL
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
    SELECT DISTINCT rq.platform_point_id
    FROM osm_pt.osm_route_quay rq
    JOIN osm_pt.osm_route r ON rq.osm_route_id = r.osm_route_id
  );
""";
    
    private static String update_osm_link_table_sql = """
-- Create a table of unique links between 2 osm quays
-- Lookup missing area_codes in the osm_missing_ifopt_area_code table
TRUNCATE TABLE osm_pt.osm_link;
INSERT INTO osm_pt.osm_link
SELECT DISTINCT rf1.osm_platform_id AS osm_quay1_id, rf1.osm_primitive_type AS osm_primitive_type1, rf1.quay_code AS quay_code1, rf1.stop_side_code AS stop_side_code1, COALESCE(rf1.stop_place_code, mac1.area_code) AS stop_place_code1,
    rf2.osm_platform_id AS osm_quay2_id, rf2.osm_primitive_type AS osm_primitive_type2, rf2.quay_code AS quay_code2, rf2.stop_side_code AS stop_side_code2, COALESCE(rf2.stop_place_code, mac2.area_code) AS stop_place_code2
FROM osm_pt.osm_route_platform rf1
JOIN osm_pt.osm_route_platform rf2 ON rf1.osm_route_id = rf2.osm_route_id AND rf2.platform_index = rf1.platform_index + 1
LEFT JOIN osm_pt.osm_missing_ifopt_area_code mac1 ON mac1.osm_id = rf1.osm_platform_id
LEFT JOIN osm_pt.osm_missing_ifopt_area_code mac2 ON mac2.osm_id = rf2.osm_platform_id           
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
            .start(sqlUpdateStep("Extract networks",update_st_network_table_sql))
            .next(sqlUpdateStep("Update network lines",update_network_line_table_sql))
            .next(sqlUpdateStep("Update routemasters",update_route_master_table_sql))
            .next(sqlUpdateStep("Update networks",update_network_table_sql))
            .next(sqlUpdateStep("Update routes", update_route_table_sql))
            .next(sqlUpdateStep("Update routemaster routes", update_route_master_route_table_sql))
            .next(sqlUpdateStep("Update quays", update_quays_table_sql))
            .next(sqlUpdateStep("Update route platforms", update_route_platform_table_sql))
            .next(sqlUpdateStep("Update route quays", update_route_quay_table_sql))
            .next(sqlUpdateStep("Update route data", update_osm_route_data_table_sql))
            .next(sqlUpdateStep("Update routemaster stop places", update_osm_route_master_stop_place_table_sql))
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