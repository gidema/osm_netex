-- SELECT network routes
SELECT DISTINCT rt.relation_id, rt.route_ref, rt.name
FROM osm_pt.osm_routes rt
JOIN osm_pt.osm_route_platform rp ON rp.osm_route_id = rt.relation_id
JOIN osm_pt.osm_platform_node pfm ON pfm.node_id = rp.osm_platform_id
WHERE ref_ifopt IS NULL AND rt.network = 'Groningen-Drenthe'
	-- AND rp.platform_location_type IN ('start', 'end')
ORDER BY rt.route_ref
