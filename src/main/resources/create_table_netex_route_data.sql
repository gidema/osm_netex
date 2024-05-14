DROP TABLE IF EXISTS netex.netex_route_data;
WITH stats AS (
  SELECT route_quay.line_number, 
  	route_quay.route_id, 
	ARRAY_AGG(route_quay.quay_code ORDER BY route_quay.quay_index) quay_list,
	ARRAY_AGG(route_quay.stopplace_code ORDER BY route_quay.quay_index) stopplace_list,
	COUNT(route_quay.quay_code) AS quay_count
  FROM netex.netex_route_quay route_quay
  GROUP BY route_quay.line_number, route_quay.route_id)
SELECT stats.*,
	start_quay.quay_code AS start_quay_code,
	end_quay.quay_code AS end_quay_code,
	start_quay.stopplace_code AS start_stopplace_code,
	end_quay.stopplace_code AS end_stopplace_code
INTO netex.netex_route_data
FROM stats
JOIN netex.netex_route_quay start_quay ON start_quay.route_id = stats.route_id AND start_quay.quay_location_type = 'start'
JOIN netex.netex_route_quay end_quay ON end_quay.route_id = stats.route_id AND end_quay.quay_location_type = 'end';
