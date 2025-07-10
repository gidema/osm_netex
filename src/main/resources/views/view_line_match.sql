DROP VIEW IF EXISTS v_all_lines;
CREATE VIEW v_all_lines AS
SELECT line."id", line.country_code, line.network, line.transport_mode, line_ref, line.netex_line_id, line.osm_line_id,
    nl.name AS netex_name, ol.name AS osm_name,
    nl.colour AS netex_colour, ol.colour AS osm_colour, line.product_category
FROM all_lines line
LEFT JOIN netex.netex_line nl ON nl.id = line.netex_line_id
LEFT JOIN osm_pt.osm_route_master ol ON ol.osm_route_master_id = line.osm_line_id
  
ALTER TABLE netex.dim_netex_route
    OWNER TO nlgis;