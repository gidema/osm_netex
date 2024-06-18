CREATE SCHEMA IF NOT EXISTS osm_pt
    AUTHORIZATION nlgis;

CREATE TABLE IF NOT EXISTS osm_pt.osm_route_data
(
    line_number character varying COLLATE pg_catalog."default",
    osm_route_id bigint,
    network character varying COLLATE pg_catalog."default",
    quay_list character varying[] COLLATE pg_catalog."default",
    stopplace_list character varying[] COLLATE pg_catalog."default",
    quay_count integer,
    start_quay_code character varying COLLATE pg_catalog."default",
    end_quay_code character varying COLLATE pg_catalog."default",
    start_stopplace_code character varying(255) COLLATE pg_catalog."default",
    end_stopplace_code character varying(255) COLLATE pg_catalog."default"
);

ALTER TABLE IF EXISTS osm_pt.osm_route_data
    OWNER to nlgis;