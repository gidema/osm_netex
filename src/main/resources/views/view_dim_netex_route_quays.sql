-- View: netex.dim_netex_route_quay

DROP VIEW netex.dim_netex_route_quay;

CREATE OR REPLACE VIEW netex.dim_netex_route_quay
 AS
 SELECT nrq.route_id,
    nrq.point_on_route_id,
    nrq.quay_index,
    nrq.quay_code,
    cq.town AS place,
    cq.quay_name,
    nrq.stop_side_code,
    nrq.stop_place_code AS stopplace_code
   FROM netex.netex_route_quay nrq
     LEFT JOIN chb.chb_quay cq ON cq.quay_code::text = nrq.quay_code::text;

ALTER TABLE netex.dim_netex_route_quay
    OWNER TO nlgis;
