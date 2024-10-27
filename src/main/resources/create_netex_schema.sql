CREATE SCHEMA IF NOT EXISTS netex
    AUTHORIZATION nlgis;

CREATE TABLE IF NOT EXISTS netex.netex_line
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    name character varying COLLATE pg_catalog."default",
    branding_ref character varying COLLATE pg_catalog."default",
    direction_type character varying COLLATE pg_catalog."default",
    transport_mode character varying COLLATE pg_catalog."default",
    public_code character varying COLLATE pg_catalog."default",
    private_code character varying COLLATE pg_catalog."default",
    colour character varying COLLATE pg_catalog."default",
    text_colour character varying COLLATE pg_catalog."default",
    mobility_impaired_access boolean,
    CONSTRAINT netex_line_pkey PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS netex.netex_line
    OWNER to nlgis;

CREATE TABLE IF NOT EXISTS netex.netex_route
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    name character varying COLLATE pg_catalog."default",
    line_ref character varying COLLATE pg_catalog."default",
    direction_type character varying COLLATE pg_catalog."default",
    CONSTRAINT netex_route_pkey PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS netex.netex_route
    OWNER to nlgis;

CREATE TABLE IF NOT EXISTS netex.netex_point_on_route
(
    point_on_route_id character varying COLLATE pg_catalog."default" NOT NULL,
    route_id character varying COLLATE pg_catalog."default",
    sequence integer,
    route_point_ref character varying COLLATE pg_catalog."default",
    CONSTRAINT netex_point_on_route_pkey PRIMARY KEY (point_on_route_id)
);

ALTER TABLE IF EXISTS netex.netex_point_on_route
    OWNER to nlgis;

CREATE TABLE IF NOT EXISTS netex.netex_quay
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    stop_area_id character varying COLLATE pg_catalog."default",
    name character varying COLLATE pg_catalog."default",
    short_name character varying COLLATE pg_catalog."default",
    rd_location geometry,
    wgs_location geometry,
    user_stop_code character varying COLLATE pg_catalog."default",
    place character varying COLLATE pg_catalog."default",
    user_stop_owner_code character varying COLLATE pg_catalog."default",
    route_point_ref character varying COLLATE pg_catalog."default",
    for_boarding boolean,
    for_alighting boolean,
    tariff_zones character varying[] COLLATE pg_catalog."default",
    CONSTRAINT netex_quay_pkey PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS netex.netex_quay
    OWNER to nlgis;

CREATE TABLE IF NOT EXISTS netex.ref_netex_ignore_user_stop_code
(
    user_stop_code character varying COLLATE pg_catalog."default" NOT NULL,
    reason character varying COLLATE pg_catalog."default",
    CONSTRAINT ref_netex_ignore_user_stop_code_pkey PRIMARY KEY (user_stop_code)
);

ALTER TABLE IF EXISTS netex.ref_netex_ignore_user_stop_code
    OWNER to postgres;

COMMENT ON TABLE netex.ref_netex_ignore_user_stop_code
    IS 'List of NeTex user stop codes that should be ignored because they are not for boarding/unboarding';
    
CREATE TABLE IF NOT EXISTS netex.netex_route_data
(
    line_number character varying COLLATE pg_catalog."default",
    route_id character varying COLLATE pg_catalog."default",
    quay_list character varying[] COLLATE pg_catalog."default",
    stopplace_list character varying[] COLLATE pg_catalog."default",
    quay_count bigint,
    start_quay_code character varying(255) COLLATE pg_catalog."default",
    end_quay_code character varying(255) COLLATE pg_catalog."default",
    start_stopplace_code character varying(255) COLLATE pg_catalog."default",
    end_stopplace_code character varying(255) COLLATE pg_catalog."default"
);

ALTER TABLE IF EXISTS netex.netex_route_data
    OWNER to postgres;
    
CREATE TABLE IF NOT EXISTS netex.netex_transport_area
(
    agency_id character varying COLLATE pg_catalog."default" NOT NULL,
    transport_area character varying COLLATE pg_catalog."default" NOT NULL,
    concession character varying COLLATE pg_catalog."default",
    CONSTRAINT netex_transport_area_pkey PRIMARY KEY (transport_area)
);

ALTER TABLE IF EXISTS netex.netex_transport_area
    OWNER to nlgis;
    
CREATE TABLE IF NOT EXISTS netex.netex_file_info
(
    agency_id character varying COLLATE pg_catalog."default" NOT NULL,
    file_name character varying COLLATE pg_catalog."default" NOT NULL,
    last_modified timestamp without time zone NOT NULL,
    size bigint NOT NULL,
    file_group character varying COLLATE pg_catalog."default",
    CONSTRAINT netex_file_pkey PRIMARY KEY (file_name)
);

ALTER TABLE IF EXISTS netex.netex_file_info
    OWNER to nlgis;
       