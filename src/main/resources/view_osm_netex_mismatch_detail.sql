WITH "match" AS (
SELECT osm_rt.relation_id AS osm_route_id, osm_rt.name AS osm_route_name, ntx_rt.name AS netex_route_name, ntx_rt.id AS netex_route_id, ntx_line.name AS netex_line_name
FROM osm_pt.osm_route_data osm
LEFT JOIN netex.netex_route_data ntx ON (ntx.start_stopplace_code = osm.start_stopplace_code
	AND ntx.end_stopplace_code = osm.end_stopplace_code)
	AND ntx.line_number = osm.line_number
--	AND ntx.quay_count - osm.quay_count BETWEEN -2 AND 2
	AND ntx.quay_list <> osm.quay_list
LEFT JOIN osm_pt.osm_routes osm_rt ON osm_rt.relation_id = osm.osm_route_id
LEFT JOIN netex.netex_route ntx_rt ON ntx_rt.id = ntx.route_id
LEFT JOIN netex.netex_line ntx_line ON ntx_line.id = ntx_rt.line_ref
WHERE osm_rt.relation_id = 5066
),
quay_idx AS (
SELECT DISTINCT match.osm_route_id, match.netex_route_id, rq.platform_index AS idx
FROM "match"
JOIN osm_pt.osm_route_quay rq ON rq.osm_route_id = match.osm_route_id
UNION
SELECT DISTINCT match.osm_route_id, match.netex_route_id, nrq.quay_index AS idx
FROM "match"
JOIN netex.netex_route_quay nrq ON nrq.route_id = match.netex_route_id
ORDER BY idx)
SELECT match.osm_route_id, match.netex_route_id, quay_idx.idx, osm_quay.ref_ifopt AS osm_quay_code, osm_quay.name AS osm_name,
	o_chb_stopplace.stopplacecode AS osm_stopplace_code, n_chb_stopplace.stopplacecode AS netex_stopplace_code,
	nrq.quay_code AS netex_quay_code, n_chb_quay.quay_name AS netex_quay_name, n_chb_quay.town AS netex_town,
	(osm_quay.ref_ifopt = nrq.quay_code) AS quay_match,
	(n_chb_stopplace.stopplacecode = o_chb_stopplace.stopplacecode) stopplace_match
FROM quay_idx
JOIN "match" ON quay_idx.osm_route_id = match.osm_route_id AND quay_idx.netex_route_id = match.netex_route_id
LEFT JOIN osm_pt.osm_route_quay rq ON rq.osm_route_id = match.osm_route_id AND rq.platform_index = quay_idx.idx
LEFT JOIN netex.netex_route_quay nrq ON nrq.route_id = match.netex_route_id AND nrq.quay_index = quay_idx.idx
LEFT JOIN osm_pt.osm_platform_node osm_quay ON osm_quay.ref_ifopt = rq.quay_code
LEFT JOIN chb.chb_quay n_chb_quay ON n_chb_quay.quaycode = nrq.quay_code
LEFT JOIN chb.chb_stop_place n_chb_stopplace ON n_chb_quay.stop_place_id = n_chb_stopplace.id
LEFT JOIN chb.chb_quay o_chb_quay ON o_chb_quay.quaycode = osm_quay.ref_ifopt
LEFT JOIN chb.chb_stop_place o_chb_stopplace ON o_chb_quay.stop_place_id = o_chb_stopplace.id
ORDER BY quay_idx.netex_route_id, quay_idx.idx;
