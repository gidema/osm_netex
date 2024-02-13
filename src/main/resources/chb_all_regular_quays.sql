SELECT quaycode AS ref_ifopt,
    stop_place_name AS "name",
    bearing,
    ST_Y(coordinates) AS lat,
    ST_X(coordinates) AS lon
FROM chb.quay
WHERE quay_type = 'regular';
