DROP TABLE chb.chb_stop_area IF EXISTS;
SELECT sp.id AS stop_place_id, sp.stopplacecode AS area_code, sp.publicname AS name,
	ST_Centroid(ST_Collect(q.wgs_location)) AS wgs_centriod, ST_Centroid(ST_Collect(rd_location)) AS rd_centroid,
	count(*) AS stop_count
INTO TABLE chb.chb_stop_area
FROM chb.chb_quay q
JOIN chb.chb_stop_place sp ON q.stop_place_id = sp.id
GROUP BY sp.id, sp.stopplacecode, sp.publicname

