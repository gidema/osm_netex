SELECT osm_rs.osm_route_id,
	osm_rt.name AS osm_name,
	ntx_rs.route_id AS netex_route_id,
	ntx_ln.name AS netex_name,
	osm_rs.quay_count,
	ntx_rs.quay_count
FROM osm_pt.osm_route_stats osm_rs
JOIN netex.netex_route_stats ntx_rs ON osm_rs.line_number = ntx_rs.line_number
	AND osm_rs.start_quay_code = ntx_rs.start_quay_code
	AND osm_rs.end_quay_code = ntx_rs.end_quay_code
LEFT JOIN netex.netex_route ntx_rt ON ntx_rt.id = ntx_rs.route_id
LEFT JOIN netex.netex_line ntx_ln ON ntx_ln.id = ntx_rt.line_ref
LEFT JOIN osm_pt.osm_routes osm_rt ON osm_rt.relation_id = osm_rs.osm_route_id
WHERE osm_rs.quay_count = ntx_rs.quay_count
ORDER BY osm_rt.name