TRUNCATE TABLE osm_netex_line_match;
WITH match AS (
  SELECT DISTINCT orme.osm_route_master_id, nle.netex_line_id
  FROM osm_pt.osm_route_master_endpoint orme
  JOIN netex.netex_line_endpoint nle ON nle.line_number = orme.line_number AND nle.stopplace_code = orme.stopplace_code)
INSERT INTO osm_netex_line_match
SELECT match.osm_route_master_id, match.netex_line_id, ol.name AS osm_line_name, nl.name AS netex_line_name, ol.network AS osm_network, osmn.country_code AS osm_country_code
FROM match 
  LEFT JOIN osm_pt.osm_route_master ol ON ol.osm_route_master_id = match.osm_route_master_id
  LEFT JOIN netex.netex_line nl ON nl.id = match.netex_line_id
  LEFT JOIN osm_pt.osm_pt_network osmn ON ol.network = osmn.network_name;
