WITH ep AS (
  SELECT route_id, 1 AS "sequence" 
  FROM netex.netex_point_on_route
  GROUP BY route_id
  UNION
  SELECT route_id, MAX("sequence") AS "sequence" 
  FROM netex.netex_point_on_route
  GROUP BY route_id)
SELECT DISTINCT psa.quay_code, cq.stop_place_name AS name, cq.town, cq.quay_status 
FROM netex.netex_point_on_route por
JOIN ep ON por.route_id = ep.route_id AND por."sequence" = ep."sequence"
JOIN netex.netex_quay quay ON quay.route_point_ref = por.route_point_ref
JOIN chb.chb_psa psa ON psa.user_stop_owner_code = quay.user_stop_owner_code
      AND psa.user_stop_code = quay.user_stop_code
LEFT JOIN chb.chb_quay cq ON cq.quaycode = psa.quay_code