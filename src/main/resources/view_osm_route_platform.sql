SELECT rt.name,
  rt.route_ref,
  rp.platform_index,
  pfm.name,
  pfm.ref_ifopt
FROM osm_pt.osm_routes rt
JOIN osm_pt.osm_route_platform rp ON rp.osm_route_id = rt.relation_id
JOIN osm_pt.osm_platform_node pfm ON pfm.node_id = rp.osm_platform_id
WHERE rt.route_ref = '7' AND rt.network = 'Veluwe-Zuid'
ORDER BY rp.osm_route_id, rp.platform_index
