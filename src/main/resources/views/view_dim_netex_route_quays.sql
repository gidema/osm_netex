DROP VIEW IF EXISTS netex.dim_netex_route_quay;
CREATE VIEW netex.dim_netex_route_quay AS 
    SELECT nrq.route_id, nrq.point_on_route_id, nrq.quay_index, nrq.quay_code, cq.place, cq.quay_name, nrq.stop_side_code, nrq.stopplace_code 
    FROM netex.netex_route_quay nrq
    LEFT JOIN chb.dim_chb_quay cq ON cq.quay_code = nrq.quay_code;

ALTER TABLE netex.dim_netex_route_quay
    OWNER TO nlgis;
