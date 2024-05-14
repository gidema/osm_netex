/**
 * 
 */
/**
 * 
 */
module HalteData {
    requires transitive chbhaltebestand;
    requires transitive spring.core;
    requires transitive org.entur.netex.java.model;
    requires spring.boot;
    requires spring.boot.starter;
    requires spring.data.jpa;
    requires spring.boot.autoconfigure;
    requires spring.data.commons;
    requires spring.context;
    requires spring.beans;
    requires jakarta.persistence;
    requires jakarta.xml.bind;
    requires jakarta.inject;
    requires org.slf4j;
    requires org.locationtech.jts;
    requires java.sql;
    requires java.xml;
    requires proj4j;
    requires org.apache.commons.csv;
    requires osmosis.core;
    requires osmosis.xml;
    requires spring.batch.infrastructure;
    requires spring.batch.core;
    requires spring.tx;
    requires spring.web;
    requires lombok;
    requires org.apache.tomcat.embed.core;
    requires spring.jcl;
    requires spring.oxm;
    requires commons.net;
    requires passengerstopassignment;
//    requires hibernate.spatial;
    
    exports nl.haltedata.osm;
    exports nl.haltedata.tools;
    exports nl.haltedata.gtfs;
    exports nl.haltedata.netex;
    exports nl.haltedata.gtfs.controllers;
    exports nl.haltedata.gtfs.config.batch;
    exports nl.haltedata.netex.controllers;
    exports nl.haltedata.netex.config.batch;
    exports nl.haltedata.chb.controllers;
    exports nl.haltedata.chb.config.batch;
    exports nl.haltedata.osm.controllers;
    exports nl.haltedata.osm.config.batch;
    
    opens nl.haltedata to spring.core, spring.context, spring.beans;
    opens nl.haltedata.tools to spring.core;
    opens nl.haltedata.osm to spring.core, spring.context, spring.beans;
    opens nl.haltedata.gtfs to spring.core, spring.context, spring.beans;
    opens nl.haltedata.gtfs.config to spring.core, spring.context, spring.beans;
    opens nl.haltedata.gtfs.config.batch to spring.core, spring.context, spring.beans;
    opens nl.haltedata.chb to spring.core, spring.context, spring.beans;
    opens nl.haltedata.chb.config to spring.core, spring.context, spring.beans;
    opens nl.haltedata.chb.config.batch to spring.core, spring.context, spring.beans;
    opens nl.haltedata.chb.mapping to spring.core, spring.beans;
    opens nl.haltedata.netex to spring.core, spring.beans;
    opens nl.haltedata.netex.osm to spring.core, spring.context, spring.beans;
    opens nl.haltedata.netex.config to spring.core, spring.context, spring.beans;
    opens nl.haltedata.netex.config.batch to spring.core, spring.context, spring.beans;
    opens nl.haltedata.netex.mapping to spring.core, spring.beans;
    opens nl.haltedata.osm.config to spring.core, spring.context, spring.beans;
    opens nl.haltedata.osm.config.batch to spring.core, spring.context, spring.beans;
    opens nl.haltedata.chb.dto;
    opens nl.haltedata.gtfs.dto;
    opens nl.haltedata.netex.dto;
    opens nl.haltedata.osm.dto;
}