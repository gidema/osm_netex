SELECT mis.osm_id, oq.osm_primitive_type, 'ref:IFOPT' AS key, cq.quaycode AS "value"
FROM osm_pt.osm_missing_ifopt_area_code mis
JOIN osm_pt.osm_quay oq ON oq.osm_id = mis.osm_id AND oq.stop_side_code IS NOT NULL
JOIN chb.chb_stop_area csa ON csa.area_code = mis.area_code
JOIN chb.chb_quay cq ON cq.stop_place_id = csa.stop_place_id
WHERE cq.stop_side_code = oq.stop_side_code