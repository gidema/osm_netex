--SELECT netex route platforms
SELECT line.name, route.line_ref, por.route_id, por.sequence, psa.quay_code, cq.stop_side_code, cq.stop_place_name AS name, cq.town, cq.quay_status 
FROM netex.netex_point_on_route por
JOIN netex.netex_quay quay ON quay.route_point_ref = por.route_point_ref
JOIN netex.netex_route route ON route.id = por.route_id
JOIN netex.netex_line line ON route.line_ref = line.id
JOIN chb.chb_psa psa ON psa.user_stop_owner_code = quay.user_stop_owner_code
      AND psa.user_stop_code = quay.user_stop_code
LEFT JOIN chb.chb_quay cq ON cq.quaycode = psa.quay_code
WHERE true
--	AND line.public_code = '101'
--	  AND line.name LIKE '%Stads%'
--    AND cq.town LIKE '%osch'
	AND route.line_ref = 'ARR:Line:26010#LZ:P443'
--	AND route.id = 'KEOLIS:Route:6702-20094'
ORDER BY por.route_id, por.sequence