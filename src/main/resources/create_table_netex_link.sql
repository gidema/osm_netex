DROP TABLE IF EXISTS netex.netex_link;
SELECT DISTINCT rq1.quay_code AS quay_code1, rq1.stop_side_code AS stop_side_code1, rq1.stopplace_code AS stopplace_code1,
	rq2.quay_code AS quay_code2, rq2.stop_side_code AS stop_side_code2, rq2.stopplace_code AS stopplace_code2
INTO netex.netex_link
FROM netex.netex_route_quay rq1
JOIN netex.netex_route_quay rq2 ON rq1.route_id = rq2.route_id AND rq2.quay_index = rq1.quay_index + 1
WHERE rq1.quay_code IS NOT NULL AND rq2.quay_code IS NOT NULL;