-- View: public.v_route_match

-- DROP VIEW public.v_route_match;

CREATE OR REPLACE VIEW public.v_route_match
 AS
SELECT match.id,
    match.line_id,
    match.osm_route_id,
    match.variant_id AS netex_variant_id,
    match.match_rate,
    match.matching,
    COALESCE(lm.network, osr.network) AS network,
    nrv.line_number AS netex_line_number,
    NULL::character varying AS netex_name,
    nrv.direction_type,
    osr.name AS osm_name,
    osr.transport_mode AS osm_transport_mode,
    osr.route_ref AS osm_line_number,
    osr."from",
    osr."to"
   FROM route_match match
     LEFT JOIN netex.netex_route_variant nrv ON nrv.id = match.variant_id
     LEFT JOIN osm_pt.osm_route osr ON osr.osm_route_id = match.osm_route_id
     LEFT JOIN line_match lm ON lm.id = match.line_id;

ALTER TABLE public.v_route_match
    OWNER TO nlgis;

