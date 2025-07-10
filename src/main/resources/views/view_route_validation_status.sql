-- View: public.route_validation_status

-- DROP VIEW public.route_validation_status;

CREATE OR REPLACE VIEW public.route_validation_status
 AS
 WITH quay_matches AS (
         SELECT DISTINCT osm.osm_route_id
           FROM osm_pt.osm_route_data osm
             JOIN netex.netex_route_variant_data ntx ON ntx.line_number::text = osm.line_number::text AND ntx.quay_list = osm.quay_list
        ), stopplace_matches AS (
         SELECT DISTINCT osm.osm_route_id
           FROM osm_pt.osm_route_data osm
             JOIN netex.netex_route_variant_data ntx ON ntx.line_number::text = osm.line_number::text AND ntx.stop_place_list = osm.stop_place_list
        ), endpoint_and_count_matches AS (
         SELECT DISTINCT osm.osm_route_id
           FROM osm_pt.osm_route_data osm
             JOIN netex.netex_route_variant_data ntx ON ntx.start_stop_place_code::text = osm.start_stop_place_code::text AND ntx.end_stop_place_code::text = osm.end_stop_place_code::text AND ntx.quay_count = osm.quay_count
        ), endpoint_matches AS (
         SELECT DISTINCT osm.osm_route_id
           FROM osm_pt.osm_route_data osm
             JOIN netex.netex_route_variant_data ntx ON ntx.start_stop_place_code::text = osm.start_stop_place_code::text AND ntx.end_stop_place_code::text = osm.end_stop_place_code::text
        )
 SELECT osm_rt.network,
    osm_rt.osm_route_id,
    osm_rt.route_ref AS line,
    osm_rt.name,
    osm_rt.transport_mode,
        CASE
            WHEN quay_match.osm_route_id IS NOT NULL THEN '1 Quays match'::text
            WHEN stop_match.osm_route_id IS NOT NULL THEN '2 Stopplaces match'::text
            WHEN endpoint_and_count_matches.osm_route_id IS NOT NULL THEN '3 Endpoints and quay count match'::text
            WHEN endpoint_matches.osm_route_id IS NOT NULL THEN '4 Endpoints match'::text
            ELSE '9 No match'::text
        END AS matching,
    oki.issue,
    osm_rt.line_number
   FROM osm_pt.osm_route osm_rt
     LEFT JOIN stopplace_matches stop_match ON stop_match.osm_route_id = osm_rt.osm_route_id
     LEFT JOIN quay_matches quay_match ON quay_match.osm_route_id = osm_rt.osm_route_id
     LEFT JOIN endpoint_and_count_matches ON endpoint_and_count_matches.osm_route_id = osm_rt.osm_route_id
     LEFT JOIN endpoint_matches ON endpoint_matches.osm_route_id = osm_rt.osm_route_id
     LEFT JOIN osm_pt.osm_route_known_issues oki ON oki.osm_route_id = osm_rt.osm_route_id;

ALTER TABLE public.route_validation_status
    OWNER TO nlgis;

