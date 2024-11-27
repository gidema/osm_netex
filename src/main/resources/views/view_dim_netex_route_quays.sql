DROP VIEW IF EXISTS netex.dim_osm_route_quay;
CREATE VIEW netex.dim_osm_route_quay AS 
    SELECT orq.route_id, orq.rank, orq.quay_code, COALESCE(qn.name, qw.name) AS quay_name,
        COALESCE(qn.stop_side_code, qw.stop_side_code) AS stop_side_code
    FROM osm_pt.osm_route_quay2 orq
    LEFT JOIN osm_pt.osm_quay qn ON qn.osm_primitive_type = 'N' AND qn.osm_id = orq.osm_quay_node_id
    LEFT JOIN osm_pt.osm_quay qw ON qn.osm_primitive_type = 'W' AND qw.osm_id = orq.osm_quay_way_id

ALTER TABLE netex.dim_osm_route_quay
    OWNER TO nlgis;
