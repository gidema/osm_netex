-- Compare osm and netex link to find quay codes for osm quays with missing quay codes
DROP TABLE IF EXISTS osm_pt.osm_missing_quay_code;
WITH candidates AS (
  SELECT ol.osm_quay1_id AS quay_id, ol.osm_primitive_type1 AS osm_primitive_type, nl.quay_code1 AS quay_code
  FROM osm_pt.osm_link ol
  JOIN netex.netex_link nl
  ON ol.stopplace_code1 = nl.stopplace_code1 AND ol.stopplace_code2 = nl.stopplace_code2
  WHERE ol.quay_code1 IS NULL
  UNION
  SELECT ol.osm_quay2_id, ol.osm_primitive_type2 AS osm_primitive_type, nl.quay_code2
  FROM osm_pt.osm_link ol
  JOIN netex.netex_link nl
  ON ol.stopplace_code1 = nl.stopplace_code1 AND ol.stopplace_code2 = nl.stopplace_code2
  WHERE ol.quay_code2 IS NULL
)
-- Restrict to candidates having 1 match
SELECT *
INTO osm_pt.osm_missing_quay_code
FROM candidates
  WHERE quay_id IN (
  SELECT quay_id
  FROM candidates
  GROUP BY quay_id
  HAVING count(*) = 1)
ORDER BY quay_code;