SELECT DISTINCT qc.quay_id AS osm_id, qc.osm_primitive_type, 'ref:IFOPT' AS key, qc.quay_code AS value
FROM osm_pt.osm_missing_quay_code qc
LEFT JOIN osm_pt.osm_route_quay rq ON qc.quay_id = rq.osm_quay_id
LEFT JOIN osm_pt.osm_route rt ON rq.osm_route_id = rt.osm_route_id
WHERE rt.network LIKE '%Noord-Holland Noord%'