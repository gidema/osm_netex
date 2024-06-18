CREATE SCHEMA IF NOT EXISTS chb
    AUTHORIZATION nlgis;

CREATE TABLE IF NOT EXISTS chb.chb_quay
(
    bearing integer,
    onlygetout boolean,
    mutationdate timestamp(6) without time zone,
    validfrom timestamp(6) without time zone,
    id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    level character varying(255) COLLATE pg_catalog."default",
    location character varying(255) COLLATE pg_catalog."default",
    quay_status character varying(255) COLLATE pg_catalog."default",
    quay_type character varying(255) COLLATE pg_catalog."default",
    quaycode character varying(255) COLLATE pg_catalog."default",
    stop_place_id character varying(255) COLLATE pg_catalog."default",
    stop_place_long_name character varying(255) COLLATE pg_catalog."default",
    stop_place_name character varying(255) COLLATE pg_catalog."default",
    street character varying(255) COLLATE pg_catalog."default",
    town character varying(255) COLLATE pg_catalog."default",
    wgs_location geometry,
    rd_location geometry,
    quay_name character varying COLLATE pg_catalog."default",
    stop_side_code character varying COLLATE pg_catalog."default",
    CONSTRAINT chb_quay_pkey PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS chb.chb_quay
    OWNER to nlgis;

CREATE TABLE IF NOT EXISTS chb.chb_stop_place
(
    mutationdate timestamp(6) without time zone,
    uiccode bigint,
    validfrom timestamp(6) without time zone,
    description character varying(255) COLLATE pg_catalog."default",
    id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    internalname character varying(255) COLLATE pg_catalog."default",
    placecode character varying(255) COLLATE pg_catalog."default",
    publicname character varying(255) COLLATE pg_catalog."default",
    publicnamelong character varying(255) COLLATE pg_catalog."default",
    publicnamemedium character varying(255) COLLATE pg_catalog."default",
    stopplacecode character varying(255) COLLATE pg_catalog."default",
    stopplaceindication character varying(255) COLLATE pg_catalog."default",
    stopplaceowner character varying(255) COLLATE pg_catalog."default",
    stopplacestatus character varying(255) COLLATE pg_catalog."default",
    stopplacetype character varying(255) COLLATE pg_catalog."default",
    street character varying(255) COLLATE pg_catalog."default",
    town character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT stop_place_pkey PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS chb.chb_stop_place
    OWNER to nlgis;

CREATE TABLE IF NOT EXISTS chb.chb_psa
(
    user_stop_owner_code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    user_stop_code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    quay_code character varying(255) COLLATE pg_catalog."default",
    quay_ref character varying(255) COLLATE pg_catalog."default",
    stopplace_code character varying(255) COLLATE pg_catalog."default",
    stopplace_ref character varying(255) COLLATE pg_catalog."default",
    user_stop_valid_from timestamp without time zone,
    user_stop_valid_thru timestamp without time zone,
    CONSTRAINT chb_psa_pkey PRIMARY KEY (user_stop_owner_code, user_stop_code)
);

ALTER TABLE IF EXISTS chb.chb_psa
    OWNER to nlgis;