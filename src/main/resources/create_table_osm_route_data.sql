DROP TABLE IF EXISTS osm_pt.osm_route_data;
WITH data AS (
  SELECT route.route_ref AS line_number,
    route.relation_id AS osm_route_id,
    route.network AS network,
    ARRAY_AGG(route_quay.quay_code ORDER BY route_quay.quay_index) quay_list,
    ARRAY_AGG(route_quay.stopplace_code ORDER BY route_quay.quay_index) stopplace_list,
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
JOIN osm_pt.osm_route_quay start_quay ON start_quay.osm_route_id = data.osm_route_id AND start_quay.quay_location_type = 'start'
JOIN osm_pt.osm_route_quay end_quay ON end_quay.osm_route_id = data.osm_route_id AND end_quay.quay_location_type = 'end';
