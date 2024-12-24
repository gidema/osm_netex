DROP VIEW IF EXISTS v_netex_quay_in_sequence;
CREATE VIEW v_netex_quay_in_sequence AS 
    SELECT qis.sequence_id, qis.quay_index, qis.line_number, qis.quay_code, quay.quay_name, qis.stop_side_code, qis.stop_place_code, qis.quay_location_type
    FROM netex.netex_quay_in_sequence qis
    LEFT JOIN chb.dim_chb_quay quay ON quay.quay_code = qis.quay_code
    ORDER BY qis.sequence_id, qis.quay_index;

ALTER TABLE v_netex_quay_in_sequence
    OWNER TO nlgis;

