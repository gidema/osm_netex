DROP VIEW IF EXISTS v_netex_route_variant_quay;
CREATE VIEW v_netex_route_variant_quay AS 
 SELECT nrvq.variant_id,
    nrvq.quay_index,
    nrvq.line_number,
    nrvq.quay_code,
    quay.quay_name,
    quay.town,
    nrvq.stop_side_code,
    nrvq.stop_place_code,
    nrvq.quay_location_type
   FROM netex.netex_route_variant_quay nrvq
     LEFT JOIN chb.chb_quay quay ON quay.quay_code::text = nrvq.quay_code::text
  ORDER BY nrvq.variant_id, nrvq.quay_index;

ALTER TABLE public.v_netex_route_variant_quay
    OWNER TO nlgis;
