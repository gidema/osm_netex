DROP TABLE IF EXISTS netex.quay_centroid;
SELECT psa.quay_code, COUNT(nq.user_stop_code) quantity, ST_Centroid(ST_Union(nq.rd_location)) rd_centroid 
INTO TABLE netex.quay_centroid
FROM netex.netex_quay nq
JOIN chb.chb_psa psa ON nq.user_stop_owner_code = psa.user_stop_owner_code AND nq.user_stop_code = psa.user_stop_code
GROUP BY psa.quay_code;