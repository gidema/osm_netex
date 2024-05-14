DROP TABLE IF EXISTS osm_pt.osm_route_platform;
SELECT sub.osm_route_id, 
  sub.osm_platform_id,
  sub.rank AS platform_index,
  sub.platform_type,
  sub.quay_code,
  sub.stopplace_code,
  CASE WHEN sub.rank=1 THEN 'start' WHEN sub.rank = sub.count THEN 'end' ELSE 'middle' END AS platform_location_type
INTO osm_pt.osm_route_platform
FROM (
  SELECT mb.relation_id AS osm_route_id,
    mb.member_id AS osm_platform_id,
    mb.sequence_id,
    mb.member_role AS platform_type,
    osm_quay.ref_ifopt AS quay_code,
	csp.stopplacecode AS stopplace_code,
    RANK() OVER (
      PARTITION BY mb.relation_id
      ORDER BY mb.sequence_id ASC
    ) AS "rank",
    COUNT(*) OVER (
      PARTITION BY mb.relation_id
    ) AS count
  FROM relation_members mb
  JOIN osm_pt.osm_routes route ON route.relation_id = mb.relation_id AND mb.member_role LIKE 'platform%' AND mb.member_type = 'N'
  LEFT JOIN osm_pt.osm_platform_node osm_quay ON osm_quay.node_id = mb.member_id
  LEFT JOIN chb.chb_quay chb_quay ON chb_quay.quaycode = osm_quay.ref_ifopt
  LEFT JOIN chb.chb_stop_place csp ON csp.id = chb_quay.stop_place_id
) AS sub;
   