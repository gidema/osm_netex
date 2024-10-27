-- View: public.v_all_routes

-- DROP VIEW public.v_all_routes;

CREATE OR REPLACE VIEW public.v_all_routes
 AS
 SELECT route.id,
    route.line_id,
    route.osm_route_id,
    route.netex_route_id,
    route.matching,
    nr.line_number AS netex_line_number,
    null::character varying AS netex_name,
    nr.direction_type,
    osr.name AS osm_name,
    osr.transport_mode AS osm_transport_mode,
    osr.route_ref AS osm_line_number,
    osr."from",
    osr."to"
   FROM all_routes route
     LEFT JOIN netex.netex_unique_route nr ON nr.id = route.netex_route_id
     LEFT JOIN osm_pt.osm_route osr ON osr.osm_route_id = route.osm_route_id;

ALTER TABLE public.v_all_routes
    OWNER TO nlgis;
