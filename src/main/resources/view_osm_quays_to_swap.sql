SELECT cq.quaycode, pn.node_id
FROM chb.chb_quay cq
JOIN chb.chb_stop_place csp ON csp.id = cq.stop_place_id
JOIN osm_pt.osm_platform_node pn ON pn.ref_ifopt = cq.quaycode
WHERE csp.stopplacecode IN (
  SELECT DISTINCT mm1.stopplacecode
  FROM osm_pt.osm_quay_mismatch mm1
  JOIN osm_pt.osm_quay_mismatch mm2
  ON mm1.stopplacecode = mm2.stopplacecode
    AND mm1.osm_quay_code = mm2.netex_quay_code
    AND mm1.netex_quay_code = mm2.osm_quay_code
  WHERE mm1.stopplacecode NOT IN (
	SELECT DISTINCT stopplacecode FROM osm_pt.osm_quay_match)	
)
ORDER BY quaycode
