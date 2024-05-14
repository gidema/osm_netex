--SELECT netex route quays
DROP TABLE IF EXISTS netex.netex_route_quay;
SELECT sub.public_code AS line_number,
  sub.route_id,
  sub.quay_code,
  sub.stopplace_code,
  sub.rank AS quay_index,
  CASE WHEN sub.rank=1 THEN 'start' WHEN sub.rank = sub.count THEN 'end' ELSE 'middle' END AS quay_location_type	
INTO netex.netex_route_quay
	FROM (
	SELECT
	line.public_code,
	por.route_id,
	psa.quay_code,
	COALESCE(psa.stopplace_code, csp.stopplacecode) AS stopplace_code,
	ROW_NUMBER() OVER (
      PARTITION BY por.route_id
      ORDER BY por.sequence ASC
    ) AS "rank",
    COUNT(*) OVER (
      PARTITION BY por.route_id
    ) AS count

  FROM netex.netex_point_on_route por
  JOIN netex.netex_quay quay ON quay.route_point_ref = por.route_point_ref
  JOIN netex.netex_route route ON route.id = por.route_id
  JOIN netex.netex_line line ON route.line_ref = line.id
  LEFT JOIN chb.chb_psa psa ON psa.user_stop_owner_code = quay.user_stop_owner_code
      AND psa.user_stop_code = quay.user_stop_code
  LEFT JOIN chb.chb_quay ON chb_quay.quaycode = psa.quay_code
  LEFT JOIN chb.chb_stop_place csp ON csp.id = chb_quay.stop_place_id
  ORDER BY por.route_id, por.sequence) AS sub