CREATE VIEW v_network_stats AS
WITH netex AS (
  SELECT network_id AS "id", COUNT(*) AS "count"
  FROM netex.netex_line "ln"
  WHERE network_id IS NOT NULL
  GROUP BY network_id),
osm AS (
  SELECT network_id AS "id", COUNT(*) AS "count"
  FROM osm_pt.st_osm_network_line onl
  JOIN osm_pt.osm_route_master orm ON orm.osm_route_master_id = onl.route_master_id
  GROUP BY id
),
"match" AS (
  SELECT network, count(*) AS count
  FROM line_match
  WHERE network IS NOT NULL AND netex_line_id IS NOT NULL AND osm_line_id IS NOT NULL
  GROUP BY network
)
SELECT nw.name AS network, COALESCE(netex.count, 0) AS netex_count, COALESCE(osm.count, 0) AS osm_count,
  COALESCE("match".count, 0) AS match_count
FROM network_match nw
LEFT JOIN netex ON netex.id = nw.dova_id
LEFT JOIN osm ON osm.id = nw.osm_id
LEFT JOIN "match" ON match.network = nw.osm_name
ORDER BY network