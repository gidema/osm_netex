DROP VIEW IF EXISTS netex.dim_netex_route;
CREATE VIEW netex.dim_netex_route AS
SELECT r.id,
  l.id AS line_id,
  l.name,
  l.branding_ref,
  r.direction_type,
  l.transport_mode,
  l.public_code,
  l.colour,
  l.text_colour,
  l.mobility_impaired_access,
  l.network
FROM netex.netex_route r
  LEFT JOIN netex.netex_line l ON l.id = r.line_ref;
  
ALTER TABLE netex.dim_netex_route
    OWNER TO nlgis;