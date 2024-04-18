SELECT ntc.quay_code, ntc.quantity, ST_Distance(cq.rd_location, ntc.rd_centroid) AS distance, cq.stop_place_name
FROM netex.quay_centroid ntc
LEFT JOIN chb.chb_quay cq ON ntc.quay_code = cq.quaycode
WHERE ST_Distance(cq.rd_location, ntc.rd_centroid) > 25
ORDER BY ST_Distance(cq.rd_location, ntc.rd_centroid) DESC
