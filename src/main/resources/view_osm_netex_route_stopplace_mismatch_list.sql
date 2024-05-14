SELECT DISTINCT osm_rt.relation_id, osm_rt.name AS osm_route_name, ntx_rt.name AS netex_route_name, ntx_line.name AS netex_line_name
FROM osm_pt.osm_route_data osm
LEFT JOIN netex.netex_route_data ntx ON (ntx.start_stopplace_code = osm.start_stopplace_code
	AND ntx.end_stopplace_code = osm.end_stopplace_code)
	AND ntx.line_number = osm.line_number
	AND ntx.quay_count - osm.quay_count BETWEEN -2 AND 2
	AND ntx.stopplace_list <> osm.stopplace_list
LEFT JOIN osm_pt.osm_routes osm_rt ON osm_rt.relation_id = osm.osm_route_id
LEFT JOIN netex.netex_route ntx_rt ON ntx_rt.id = ntx.route_id
LEFT JOIN netex.netex_line ntx_line ON ntx_line.id = ntx_rt.line_ref
WHERE osm.network LIKE '%Groningen-Drenthe%' AND ntx_rt.name IS NOT NULL
	AND osm_rt.relation_id NOT IN (
  SELECT DISTINCT osm_rt.relation_id
  FROM osm_pt.osm_route_data osm
  JOIN netex.netex_route_data ntx ON ntx.line_number = osm.line_number
	AND ntx.stopplace_list = osm.stopplace_list
  -- AND ntx.quay_list = osm.quay_list
  LEFT JOIN osm_pt.osm_routes osm_rt ON osm_rt.relation_id = osm.osm_route_id
  LEFT JOIN netex.netex_route ntx_rt ON ntx_rt.id = ntx.route_id
  LEFT JOIN netex.netex_line ntx_line ON ntx_line.id = ntx_rt.line_ref)
ORDER BY osm_rt.name
