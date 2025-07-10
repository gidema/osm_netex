--DROP VIEW IF EXISTS osm_pt.dim_osm_route_quay;
--CREATE VIEW osm_pt.dim_osm_route_quay AS 
--    SELECT orq.osm_route_id, orq.quay_index, orq.quay_code,
--        COALESCE(qn.name, qw.name) AS quay_name,
--        COALESCE(qn.stop_side_code, qw.stop_side_code) AS stop_side_code,
--        chbq.area_code,
--        orq.osm_quay_node_id,
--        orq.osm_quay_way_id
--    FROM osm_pt.osm_route_quay2 orq
--    LEFT JOIN osm_pt.osm_quay qn ON qn.osm_primitive_type = 'N' AND qn.osm_id = orq.osm_quay_node_id
--    LEFT JOIN osm_pt.osm_quay qw ON qn.osm_primitive_type = 'W' AND qw.osm_id = orq.osm_quay_way_id
--    LEFT JOIN chb.dim_chb_quay chbq ON chbq.quay_code = orq.quay_code;
--    
--ALTER TABLE osm_pt.dim_osm_route_quay
--    OWNER TO nlgis;

DROP VIEW IF EXISTS osm_pt.dim_osm_route_quay;
CREATE VIEW osm_pt.dim_osm_route_quay AS 
    SELECT osm_route_id, quay_index, quay_code, quay_name,stop_side_code, area_code, 
    platform_point_id AS osm_quay_node_id, platform_area_id AS osm_quay_way_id
    FROM osm_pt.osm_route_quay
    
ALTER TABLE osm_pt.dim_osm_route_quay;
    OWNER TO nlgis;
