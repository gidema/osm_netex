DROP VIEW chb.dim_chb_quay;

CREATE VIEW chb.dim_chb_quay AS
SELECT quay.quay_code,
  quay.quay_name,
  quay.stop_side_code,
  sp.stop_place_code AS area_code,
  quay.quay_status, 
  quay.quay_type, 
  sp.stop_place_type AS area_type,
  quay.bearing,
  COALESCE(quay.town, sp.town) AS place,
  quay.transport_modes
FROM chb.chb_quay quay
  LEFT JOIN chb.chb_stop_place sp ON sp.id = quay.stop_place_id;

ALTER TABLE chb.dim_chb_quay
    OWNER TO nlgis;