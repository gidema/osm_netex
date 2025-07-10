-- View: osm_pt.dim_osm_route_platform

-- DROP VIEW osm_pt.dim_osm_route_platform;

CREATE OR REPLACE VIEW osm_pt.dim_osm_route_platform
 AS
 SELECT orq.osm_route_id,
    orq.quay_index AS platform_index,
    orq.quay_code,
    orq.quay_name,
    orq.stop_side_code,
    chbq.area_code
   FROM osm_pt.osm_route_platform orq
     LEFT JOIN chb.dim_chb_quay_deprecated chbq ON chbq.quay_code::text = orq.quay_code::text;

ALTER TABLE osm_pt.dim_osm_route_platform
    OWNER TO nlgis;

