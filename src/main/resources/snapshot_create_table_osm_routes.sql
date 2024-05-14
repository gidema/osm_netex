DROP TABLE IF EXISTS osm_pt.osm_routes;
SELECT rt.id AS relation_id,
  CAST((rt.tags->'route') AS CHARACTER VARYING) AS transport_mode,
  CAST((rt.tags->'name') AS CHARACTER VARYING) AS "name",
  CAST((rt.tags->'operator') AS CHARACTER VARYING) AS operator,
  CAST((rt.tags->'ref') AS CHARACTER VARYING) AS route_ref,
  CAST((rt.tags->'network') AS CHARACTER VARYING) AS network,
  CAST((rt.tags->'from') AS CHARACTER VARYING) AS from,
  CAST((rt.tags->'to') AS CHARACTER VARYING) AS to
INTO osm_pt.osm_routes
FROM relations rt
WHERE rt.tags->'type' = 'route' AND rt.tags->'route'  IN ('bus', 'tram', 'train')
