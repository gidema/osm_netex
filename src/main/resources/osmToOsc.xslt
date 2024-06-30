<?xml version="1.0" encoding="UTF-8"?>
<!--
  XSLT to create a osm changeset file (*.osc) from an osm file.
  This can be used to add extend a dataset imported from a Planet file with (restricted) number
  of entities outside the area of the planet file. For example foreign bus stops of a bus route that 
  extends into a neighbouring country.
  Workflow:
  - Create a list of id's for the missing entities by means of an SQL query for example
  - Retreive an osm file containing the missing entities from overpath or the osm API.
  - Use this xslt to create an osc file from the osm file
  - Use Osmosis write-pgsql-change to write the change to the database
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<osmChange version="0.6">
  <create>
    <xsl:for-each select="osm/node">
      <node uid="0" user="" changeset="0">
        <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
        <xsl:attribute name="version"><xsl:value-of select="@version"/></xsl:attribute>
        <xsl:attribute name="lat"><xsl:value-of select="@lat"/></xsl:attribute>
        <xsl:attribute name="lon"><xsl:value-of select="@lon"/></xsl:attribute>
        <xsl:attribute name="timestamp"><xsl:value-of select="@timestamp"/></xsl:attribute>
        <xsl:copy-of select="tag"/>
      </node>
    </xsl:for-each>
  </create>
</osmChange>
</xsl:template>
</xsl:stylesheet>
