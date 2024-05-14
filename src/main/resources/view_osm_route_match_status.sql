WITH stopplace_matches AS (
	  SELECT DISTINCT osm.osm_route_id
  FROM osm_pt.osm_route_data osm
  JOIN netex.netex_route_data ntx ON ntx.line_number = osm.line_number
	AND ntx.stopplace_list = osm.stopplace_list
--  LEFT JOIN osm_pt.osm_routes osm_rt ON osm_rt.relation_id = osm.osm_route_id
--  LEFT JOIN netex.netex_route ntx_rt ON ntx_rt.id = ntx.route_id
--  LEFT JOIN netex.netex_line ntx_line ON ntx_line.id = ntx_rt.line_ref
),
quay_matches AS (
	  SELECT DISTINCT osm.osm_route_id
  FROM osm_pt.osm_route_data osm
  JOIN netex.netex_route_data ntx ON ntx.line_number = osm.line_number
    AND ntx.quay_list = osm.quay_list
--  LEFT JOIN osm_pt.osm_routes osm_rt ON osm_rt.relation_id = osm.osm_route_id
--  LEFT JOIN netex.netex_route ntx_rt ON ntx_rt.id = ntx.route_id
--  LEFT JOIN netex.netex_line ntx_line ON ntx_line.id = ntx_rt.line_ref
),
endpoint_and_count_matches AS (SELECT DISTINCT osm.osm_route_id
FROM osm_pt.osm_route_data osm
JOIN netex.netex_route_data ntx ON (ntx.start_stopplace_code = osm.start_stopplace_code
	AND ntx.end_stopplace_code = osm.end_stopplace_code
	AND ntx.quay_count = osm.quay_count)
--LEFT JOIN osm_pt.osm_routes osm_rt ON osm_rt.relation_id = osm.osm_route_id
--LEFT JOIN netex.netex_route ntx_rt ON ntx_rt.id = ntx.route_id
--LEFT JOIN netex.netex_line ntx_line ON ntx_line.id = ntx_rt.line_ref
),
endpoint_matches AS (SELECT DISTINCT osm.osm_route_id
FROM osm_pt.osm_route_data osm
JOIN netex.netex_route_data ntx ON (ntx.start_stopplace_code = osm.start_stopplace_code
	AND ntx.end_stopplace_code = osm.end_stopplace_code)
--LEFT JOIN osm_pt.osm_routes osm_rt ON osm_rt.relation_id = osm.osm_route_id
--LEFT JOIN netex.netex_route ntx_rt ON ntx_rt.id = ntx.route_id
--LEFT JOIN netex.netex_line ntx_line ON ntx_line.id = ntx_rt.line_ref
)
SELECT osm_rt.network, osm_rt.relation_id AS osm_route_id, osm_rt.name, osm_rt.transport_mode,
	CASE 
	WHEN quay_match.osm_route_id IS NOT NULL THEN '1 Quays match'
	WHEN stop_match.osm_route_id IS NOT NULL THEN '2 Stopplaces match'
	WHEN endpoint_and_count_matches.osm_route_id IS NOT NULL THEN '3 Endpoints and quay count match'
	WHEN endpoint_matches.osm_route_id IS NOT NULL THEN '4 Endpoints match'
	ELSE '9 No match'
	END AS matching
FROM osm_pt.osm_routes osm_rt
LEFT JOIN stopplace_matches stop_match ON stop_match.osm_route_id = osm_rt.relation_id
LEFT JOIN quay_matches quay_match ON quay_match.osm_route_id = osm_rt.relation_id
LEFT JOIN endpoint_and_count_matches ON endpoint_and_count_matches.osm_route_id = osm_rt.relation_id
LEFT JOIN endpoint_matches ON endpoint_matches.osm_route_id = osm_rt.relation_id