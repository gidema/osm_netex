DROP TABLE IF EXISTS osm_pt.osm_platform_node;
SELECT nd.id AS node_id,
  nd.geom AS wgs_location,
  ST_TRANSFORM(nd.geom, 28992) AS rd_location,
  CAST((nd.tags->'public_transport') AS character varying) AS platform_type,
  CAST((nd.tags->'name') AS character varying) AS "name",
  CAST((nd.tags->'bus') AS character varying) AS is_bus,
  CAST((nd.tags->'ref:IFOPT') AS character varying) AS ref_ifopt,
  CAST((nd.tags->'note') AS character varying) AS note
INTO osm_pt.osm_platform_node
FROM nodes nd
WHERE nd.tags->'public_transport' LIKE 'platform%'
