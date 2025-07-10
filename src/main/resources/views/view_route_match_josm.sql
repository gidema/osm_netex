DROP VIEW IF EXISTS route_match_josm;

CREATE VIEW route_match_josm AS (
--  SELECT osm_rt.osm_route_id AS osm_route_id, 
--    osm_rt.name AS osm_route_name,
--    nrv.id AS netex_route_variant_id,
--    ntx_line.name AS netex_line_name,
--    nrv.direction_type AS netex_direction,
--    CAST(ABS(ntx.quay_count - osm.quay_count) AS INTEGER) AS quay_count_difference,
--    (ntx.start_stop_place_code = osm.start_stop_place_code) AS start_area_match,
--    (ntx.end_stop_place_code = osm.end_stop_place_code) AS end_area_match
--  FROM osm_pt.osm_route_data osm
--  INNER JOIN netex.netex_route_variant_data ntx ON (ntx.start_stop_place_code = osm.start_stop_place_code
--    OR ntx.end_stop_place_code = osm.end_stop_place_code)
--    AND ntx.line_number = osm.line_number
--  LEFT JOIN osm_pt.osm_route osm_rt ON osm_rt.osm_route_id = osm.osm_route_id
--  LEFT JOIN netex.netex_route_variant nrv ON nrv.id = ntx.variant_id
--  LEFT JOIN netex.netex_line ntx_line ON ntx_line.id = nrv.line_ref)
  SELECT rm.osm_route_id, 
    osm_rt.name AS osm_route_name,
    nrv.id AS netex_route_variant_id,
    nl.name AS netex_line_name,
    nrv.direction_type AS netex_direction,
    CAST(ABS(nrv.quay_count - ord.quay_count) AS INTEGER) AS quay_count_difference,
    (nrv.start_stop_place_code = ord.start_stop_place_code) AS start_area_match,
    (nrv.end_stop_place_code = ord.end_stop_place_code) AS end_area_match
  FROM route_match rm
  LEFT JOIN osm_pt.osm_route osm_rt ON osm_rt.osm_route_id = rm.osm_route_id
  LEFT JOIN osm_pt.osm_route_data ord ON ord.osm_route_id = rm.osm_route_id
  LEFT JOIN netex.netex_route_variant nrv ON nrv.id = rm.variant_id
  LEFT JOIN netex.netex_line nl ON nl.id = nrv.line_ref);

ALTER TABLE public.route_match_josm
    OWNER TO nlgis;
