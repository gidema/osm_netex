/**
 * 
 */
/**
 * 
 */
module HalteData {
//    requires transitive chbhaltebestand;
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
    requires transitive proj4j;
//    requires org.apache.commons.csv;
    requires transitive osmosis.core;
    requires transitive osmosis.xml;
    requires spring.batch.infrastructure;
    requires spring.batch.core;
    requires spring.tx;
    requires spring.web;
    requires lombok;
    requires org.apache.tomcat.embed.core;
    requires spring.jcl;
    requires spring.oxm;
    requires transitive commons.net;
//    requires passengerstopassignment;
    requires java.desktop;
    requires java.net.http;
    requires org.hibernate.orm.core;
//    requires hibernate.spatial;
    
    exports nl.haltedata.cli;
    exports nl.haltedata.backend;
    exports nl.haltedata.osm;
    exports nl.haltedata.tools;
//    exports nl.haltedata.gtfs;
    exports nl.haltedata.analysis;
//    exports nl.haltedata.gtfs.controllers;
//    exports nl.haltedata.gtfs.config.batch;
    exports nl.haltedata.netex.controllers;
    exports nl.haltedata.chb.controllers;
    exports nl.haltedata.chb.config.batch;
    exports nl.haltedata.osm.controllers;
    exports nl.haltedata.osm.config.batch;
    exports nl.haltedata.analysis.controllers;
    exports nl.haltedata.analysis.etl;
    
    opens nl.haltedata.cli to  spring.core;
    opens nl.haltedata.backend to spring.core;
    opens nl.haltedata.tools to spring.core;
    opens nl.haltedata.osm to spring.core, spring.context, spring.beans;
//    opens nl.haltedata.gtfs to spring.core, spring.context, spring.beans;
//    opens nl.haltedata.gtfs.config to spring.core, spring.context, spring.beans;
//    opens nl.haltedata.gtfs.config.batch to spring.core, spring.context, spring.beans;
    opens nl.haltedata.chb to spring.core, spring.context, spring.beans;
    opens nl.haltedata.chb.config to spring.core, spring.context, spring.beans;
    opens nl.haltedata.chb.config.batch to spring.core, spring.context, spring.beans;
    opens nl.haltedata.osm.config to spring.core, spring.context, spring.beans;
    opens nl.haltedata.osm.config.batch to spring.core, spring.context, spring.beans;
    opens nl.haltedata.analysis.etl to spring.core, spring.context, spring.beans;
    opens nl.haltedata.osm.controllers to spring.core;
    opens nl.haltedata.analysis.controllers to spring.core;
    opens nl.haltedata.netex.controllers to spring.core;
    opens nl.haltedata.chb.controllers to spring.core;
    opens nl.haltedata.analysis to spring.core;
    opens nl.haltedata.chb.dto;
//    opens nl.haltedata.gtfs.dto;
    opens nl.haltedata.netex.dto;
    opens nl.haltedata.osm.dto;
    opens nl.haltedata.analysis.dto;
}