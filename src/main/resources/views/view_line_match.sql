-- View: public.v_line_match

DROP VIEW public.v_line_match;

CREATE OR REPLACE VIEW public.v_line_match
 AS
 SELECT line.id,
    line.administrative_zone,
    COALESCE(nm.name, ol.network) AS network,
    line.transport_mode,
    line.line_number,
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
     LEFT JOIN osm_pt.osm_line ol ON ol.id = line.osm_line_id
     LEFT JOIN network_match nm ON nm.administrative_zone::text = line.administrative_zone::text;

ALTER TABLE public.v_line_match
    OWNER TO nlgis;

