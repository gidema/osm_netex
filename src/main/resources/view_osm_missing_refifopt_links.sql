SELECT ol.osm_quay1, ol.osm_quay2, COALESCE(ol.stopplace_code1, mac1.area_code) AS area_code1, COALESCE(ol.stopplace_code2, mac2.area_code) AS area_code2
  FROM osm_pt.osm_link ol 
  LEFT JOIN osm_pt.osm_missing_ifopt_area_code mac1 ON mac1.osm_id = ol.osm_quay1
  LEFT JOIN osm_pt.osm_missing_ifopt_area_code mac2 ON mac2.osm_id = ol.osm_quay2
  WHERE (ol.stopplace_code1 IS NULL OR ol.stopplace_code2 IS NULL)
    AND COALESCE(ol.stopplace_code1, mac1.area_code) IS NOT NULL
    AND COALESCE(ol.stopplace_code2, mac2.area_code) IS NOT NULL

