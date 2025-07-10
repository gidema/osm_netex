DROP VIEW IF EXISTS route_match_josm;
CREATE VIEW route_match_josm AS (
  SELECT osm_rt.osm_route_id AS osm_route_id, 
    osm_rt.name AS osm_route_name,
    ntx_rt.id AS netex_route_id,
    ntx_line.name AS netex_line_name,
    ntx_rt.direction_type AS netex_direction,
    CAST(ABS(ntx.quay_count - osm.quay_count) AS INTEGER) AS quay_count_difference,
    (ntx.start_stopplace_code = osm.start_stopplace_code) AS start_area_match,
    (ntx.end_stopplace_code = osm.end_stopplace_code) AS end_area_match
  FROM osm_pt.osm_route_data osm
  INNER JOIN netex.netex_route_data ntx ON (ntx.start_stopplace_code = osm.start_stopplace_code
	OR ntx.end_stopplace_code = osm.end_stopplace_code)
	AND ntx.line_number = osm.line_number
  LEFT JOIN osm_pt.osm_route osm_rt ON osm_rt.osm_route_id = osm.osm_route_id
  LEFT JOIN netex.netex_route ntx_rt ON ntx_rt.id = ntx.route_id
  LEFT JOIN netex.netex_line ntx_line ON ntx_line.id = ntx_rt.line_ref)