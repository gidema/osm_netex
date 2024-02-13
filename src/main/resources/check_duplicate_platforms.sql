SELECT node_id, ref_ifopt
FROM osm_platform.platform_nodes
WHERE ref_ifopt IN (
    SELECT ref_ifopt
    FROM (SELECT ref_ifopt, COUNT(node_id) AS number
  FROM osm_platform.platform_nodes
  WHERE ref_ifopt IS NOT NULL
  GROUP BY ref_ifopt
  HAVING COUNT(node_id) > 1) AS duplicates)
ORDER BY ref_ifopt