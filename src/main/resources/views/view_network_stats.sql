-- View: public.v_network_stats

-- DROP VIEW public.v_network_stats;

CREATE OR REPLACE VIEW public.v_network_stats
 AS
 WITH netex AS (
         SELECT ln.administrative_zone,
            count(*) AS count
           FROM netex.netex_line ln
          WHERE ln.network_id IS NOT NULL
          GROUP BY ln.administrative_zone
        ), osm AS (
         SELECT onl.administrative_zone AS id,
            count(*) AS count
           FROM osm_pt.st_osm_network_line onl
             JOIN osm_pt.osm_route_master orm ON orm.osm_route_master_id = onl.route_master_id
          GROUP BY onl.administrative_zone
        ), match AS (
         SELECT line_match.network_id,
            count(*) AS count
           FROM line_match
          WHERE line_match.network_id IS NOT NULL AND line_match.netex_line_id IS NOT NULL AND line_match.osm_line_id IS NOT NULL
          GROUP BY line_match.network_id
        )
 SELECT nw.name AS network,
    COALESCE(netex.count, 0::bigint) AS netex_count,
    COALESCE(osm.count, 0::bigint) AS osm_count,
    COALESCE(match.count, 0::bigint) AS match_count
   FROM network_match nw
     LEFT JOIN netex ON netex.administrative_zone = nw.administrative_zone
     LEFT JOIN osm ON osm.administrative_zone = nw.administrative_zone
     LEFT JOIN match ON match.network_id = nw.network_id
  ORDER BY nw.name;

ALTER TABLE public.v_network_stats
    OWNER TO postgres;

