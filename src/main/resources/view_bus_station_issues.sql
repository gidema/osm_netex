WITH "match" AS (
SELECT osm_rt.relation_id AS osm_route_id, osm_rt.name AS osm_route_name, ntx_rt.name AS netex_route_name, ntx_rt.id AS netex_route_id, ntx_line.name AS netex_line_name
FROM osm_pt.osm_route_data osm
LEFT JOIN netex.netex_route_data ntx ON (ntx.start_stopplace_code = osm.start_stopplace_code
	AND ntx.end_stopplace_code = osm.end_stopplace_code)
	AND ntx.line_number = osm.line_number
	AND ntx.quay_count - osm.quay_count BETWEEN -2 AND 2
	AND ntx.quay_list <> osm.quay_list
LEFT JOIN osm_pt.osm_routes osm_rt ON osm_rt.relation_id = osm.osm_route_id
LEFT JOIN netex.netex_route ntx_rt ON ntx_rt.id = ntx.route_id
LEFT JOIN netex.netex_line ntx_line ON ntx_line.id = ntx_rt.line_ref
)
SELECT DISTINCT n_chb_quay.quay_name AS netex_quay_name, n_chb_quay.town AS netex_town,
	match.osm_route_id, osm_rt.name, osm_quay.ref_ifopt AS osm_quay_code, nrq.quay_code AS netex_quay_code, rq.platform_index AS osm_index,
	o_chb_stopplace.stopplacecode
FROM "match"
JOIN osm_pt.osm_route_quay rq ON rq.osm_route_id = match.osm_route_id
LEFT JOIN osm_pt.osm_platform_node osm_quay ON osm_quay.ref_ifopt = rq.quay_code
LEFT JOIN osm_pt.osm_routes osm_rt ON osm_rt.relation_id = rq.osm_route_id
LEFT JOIN netex.netex_route_quay nrq ON nrq.route_id = match.netex_route_id
LEFT JOIN chb.chb_quay n_chb_quay ON n_chb_quay.quaycode = nrq.quay_code
LEFT JOIN chb.chb_stop_place n_chb_stopplace ON n_chb_quay.stop_place_id = n_chb_stopplace.id
LEFT JOIN chb.chb_quay o_chb_quay ON o_chb_quay.quaycode = osm_quay.ref_ifopt
LEFT JOIN chb.chb_stop_place o_chb_stopplace ON o_chb_quay.stop_place_id = o_chb_stopplace.id
WHERE rq.platform_index = nrq.quay_index 
	AND osm_rt.network = 'Groningen-Drenthe'
	AND osm_quay.ref_ifopt <> nrq.quay_code 
	AND n_chb_stopplace.stopplacecode = o_chb_stopplace.stopplacecode
ORDER BY o_chb_stopplace.stopplacecode
