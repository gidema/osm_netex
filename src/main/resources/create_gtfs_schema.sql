CREATE SCHEMA IF NOT EXISTS gtfs
    AUTHORIZATION nlgis;

CREATE TABLE IF NOT EXISTS gtfs.gtfs_agency
(
    agency_id character varying COLLATE pg_catalog."default" NOT NULL,
    agency_name character varying COLLATE pg_catalog."default" NOT NULL,
    agency_url character varying COLLATE pg_catalog."default",
    agency_timezone character varying COLLATE pg_catalog."default",
    agency_phone character varying COLLATE pg_catalog."default",
    CONSTRAINT gtfs_agency_pkey PRIMARY KEY (agency_id)
);

ALTER TABLE IF EXISTS gtfs.gtfs_agency
    OWNER to nlgis;

CREATE TABLE IF NOT EXISTS gtfs.gtfs_quay
(
    wheelchair_boarding boolean,
    quay_id bigint NOT NULL,
    stop_area_id bigint,
    name character varying(255) COLLATE pg_catalog."default",
    place character varying(255) COLLATE pg_catalog."default",
    quay_code character varying(255) COLLATE pg_catalog."default",
    ref_ifopt character varying(255) COLLATE pg_catalog."default",
    coordinates geometry,
    operator character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT gtfs_quay_pkey PRIMARY KEY (quay_id)
);

ALTER TABLE IF EXISTS gtfs.gtfs_quay
    OWNER to nlgis;

CREATE TABLE IF NOT EXISTS gtfs.gtfs_stop_time
(
    drop_off_type integer,
    fare_units_traveled double precision,
    pickup_type integer,
    shape_dist_traveled double precision,
    stop_sequence integer,
    timepoint integer,
    stop_id bigint,
    stop_time_id bigserial,
    trip_id bigint,
    arrival_time character varying(255) COLLATE pg_catalog."default",
    departure_time character varying(255) COLLATE pg_catalog."default",
    stop_headsign character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT gtfs_stop_time_pkey PRIMARY KEY (stop_time_id)
);

ALTER TABLE IF EXISTS gtfs.gtfs_stop_time
    OWNER to nlgis;
