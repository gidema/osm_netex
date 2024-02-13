SELECT nt1.node_id, nt2.v AS name, nt3.v AS ref_ifopt, nt4.v AS bus, nt5.v AS "zone", nt6.v AS cxx_code,
CASE
  WHEN nt3.v IS NOT NULL THEN nt3.v
  WHEN nt6.v IS NOT NULL THEN 'NL:Q:' || nt6.v
  ELSE NULL
END AS ref,
CASE
  WHEN nt5.v IS NOT NULL THEN nt5.v
  WHEN nt3.v IS NOT NULL THEN SUBSTR(nt3.v, 6, 4)
  WHEN nt6.v IS NOT NULL THEN SUBSTR(nt6.v, 1, 4)
  ELSE NULL
END AS ref_zone
INTO TABLE osm_pt.platform_nodes
FROM node_tags nt1
JOIN node_tags nt2 ON nt1.node_id = nt2.node_id AND nt2.k = 'name'
LEFT JOIN node_tags nt3 ON nt1.node_id = nt3.node_id AND nt3.k = 'ref:IFOPT'
LEFT JOIN node_tags nt4 ON nt1.node_id = nt4.node_id AND nt4.k = 'bus'
LEFT JOIN node_tags nt5 ON nt1.node_id = nt5.node_id AND nt5.k = 'zone'
LEFT JOIN node_tags nt6 ON nt1.node_id = nt6.node_id AND nt6.k = 'cxx:code'
WHERE nt1.k = 'public_transport' AND nt1.v = 'platform';


SELECT wt1.way_id, wt2.v AS name, wt3.v AS ref_ifopt, wt4.v AS bus, wt5.v AS "zone", wt6.v AS cxx_code,
CASE
  WHEN wt3.v IS NOT NULL THEN wt3.v
  WHEN wt6.v IS NOT NULL THEN 'NL:Q:' || wt6.v
  ELSE NULL
END AS ref,
CASE
  WHEN wt5.v IS NOT NULL THEN wt5.v
  WHEN wt3.v IS NOT NULL THEN SUBSTR(wt3.v, 6, 4)
  WHEN wt6.v IS NOT NULL THEN SUBSTR(wt6.v, 1, 4)
  ELSE NULL
END AS ref_zone
INTO TABLE osm_pt.platform_ways
FROM way_tags wt1
JOIN way_tags wt2 ON wt1.way_id = wt2.way_id AND wt2.k = 'name'
LEFT JOIN way_tags wt3 ON wt1.way_id = wt3.way_id AND wt3.k = 'ref:IFOPT'
LEFT JOIN way_tags wt4 ON wt1.way_id = wt4.way_id AND wt4.k = 'bus'
LEFT JOIN way_tags wt5 ON wt1.way_id = wt5.way_id AND wt5.k = 'zone'
LEFT JOIN way_tags wt6 ON wt1.way_id = wt6.way_id AND wt6.k = 'cxx:code'
WHERE wt1.k = 'public_transport' AND wt1.v = 'platform';