--SELECT netex route platforms
SELECT DISTINCT line.name, route.line_ref, por.route_id
FROM netex.netex_point_on_route por
JOIN netex.netex_quay quay ON quay.route_point_ref = por.route_point_ref
JOIN netex.netex_route route ON route.id = por.route_id
JOIN netex.netex_line line ON route.line_ref = line.id
JOIN chb.chb_psa psa ON psa.user_stop_owner_code = quay.user_stop_owner_code
      AND psa.user_stop_code = quay.user_stop_code
LEFT JOIN chb.chb_quay cq ON cq.quaycode = psa.quay_code
WHERE true
	AND line.public_code = '102'
--	  AND line.name LIKE '%Stads%'
--    AND cq.town LIKE '%osch'	
--	AND route.line_ref = 'ARR:Line:51101#FY:P376'
ORDER BY por.route_id