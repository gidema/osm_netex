SELECT rm.network, lm.line_ref, lm.osm_name, lm.netex_name, 
  COALESCE(COUNT(*) FILTER ( WHERE matching = 'Quays match')) AS QuaysMatch,
  COALESCE(COUNT(*) FILTER ( WHERE matching = 'Stopplaces match')) AS StopplacesMatch,
  COALESCE(COUNT(*) FILTER ( WHERE matching = 'Endpoints and quay count match')) AS EndpointsAndCountMatch,
  COALESCE(COUNT(*) FILTER ( WHERE matching = 'Endpoints match')) AS EndpointsMatch   
FROM public.v_route_match rm
JOIN v_line_match lm ON lm.id = rm.line_id
WHERE rm.network = 'Twente'
GROUP BY rm.network, lm.line_ref, lm.osm_name, lm.netex_name
ORDER BY rm.network, lm.line_ref;
