TRUNCATE TABLE osm_pt.osm_route_quay2;
INSERT INTO osm_pt.osm_route_quay2 (osm_route_id,"rank", osm_quay_node_id, osm_quay_way_id, quay_code,
    stop_side_code_n, stop_side_code_w, area_code)
SELECT osm_route_id,
  ROW_NUMBER() OVER (
      PARTITION BY osm_route_id
      ORDER BY quay_index ASC
    ) AS "rank",
    osm_quay_node_id,
    osm_quay_way_id,
    quay_code,
    stop_side_code_n,
    stop_side_code_w,
    stopplace_code AS area_code
FROM (
SELECT COALESCE(rqn.osm_route_id, rqw.osm_route_id) AS osm_route_id,
  LEAST(rqn.quay_index, rqw.quay_index) AS quay_index,
  rqn.osm_quay_id AS osm_quay_node_id, 
  rqw.osm_quay_id AS osm_quay_way_id, 
  COALESCE(rqn.quay_code, rqw.quay_code) AS quay_code,
  rqn.stop_side_code AS stop_side_code_n,
  rqn.stop_side_code AS stop_side_code_w,
  COALESCE(rqn.stopplace_code, rqw.stopplace_code) AS stopplace_code
FROM osm_pt.osm_route_quay rqn
FULL OUTER JOIN osm_pt.osm_route_quay rqw 
  ON rqn.osm_route_id = rqw.osm_route_id 
  AND rqn.quay_code = rqw.quay_code
  AND ABS(rqn.quay_index - rqw.quay_index) = 1
WHERE TRUE
  AND (rqn.osm_primitive_type = 'N' OR rqn.osm_primitive_type IS NULL)
  AND (rqw.osm_primitive_type = 'W' OR rqw.osm_primitive_type IS NULL)
) AS sub;