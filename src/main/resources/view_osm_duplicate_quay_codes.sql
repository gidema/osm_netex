WITH dup AS (
SELECT ref_ifopt AS quay_code
FROM osm_pt.osm_platform_node
WHERE ref_ifopt IS NOT NULL
GROUP BY ref_ifopt
HAVING COUNT(node_id) > 1)
SELECT node_id, ref_ifopt AS quay_code
FROM osm_pt.osm_platform_node
JOIN dup ON dup.quay_code = osm_platform_node.ref_ifopt
ORDER BY dup.quay_code
