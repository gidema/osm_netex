CREATE OR REPLACE VIEW public.v_line_match
 AS
 SELECT line.id,
    line.country_code,
    line.network,
    line.transport_mode,
    line.line_ref,
    line.netex_line_id,
    line.osm_line_id,
    nl.name AS netex_name,
    ol.name AS osm_name,
    nl.colour AS netex_colour,
    ol.colour AS osm_colour,
    line.product_category,
    line.line_sort
   FROM line_match line
     LEFT JOIN netex.netex_line nl ON nl.id::text = line.netex_line_id::text
     LEFT JOIN osm_pt.osm_route_master ol ON ol.osm_route_master_id = line.osm_line_id;

ALTER TABLE public.v_line_match
    OWNER TO nlgis;

