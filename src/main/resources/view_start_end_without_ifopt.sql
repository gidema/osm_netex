SELECT DISTINCT osm_platform_id, quay_code
FROM osm_pt.osm_route_quay rq1
JOIN osm_pt.osm_route_quay rq2 ON rq1.osm_route_id = rq2.osm_route_id AND rq2.
WHERE rq.platform_location_type IN ('start','end')
AND r.network LIKE '%Brabant%'
AND rq.quay_code IS NULL
