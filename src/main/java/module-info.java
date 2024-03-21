/**
 * 
 */
/**
 * 
 */
module HalteData {
    requires transitive chbhaltebestand;
    requires spring.boot;
    requires spring.boot.starter;
    requires spring.data.jpa;
    requires spring.core;
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
//    requires hibernate.spatial;
    
    exports nl.haltedata.gtfs;
    exports nl.haltedata.gtfs.controllers;
    exports nl.haltedata.gtfs.config.batch;
    opens nl.haltedata.chb to spring.core, spring.context, spring.beans;
    opens nl.haltedata.chb.dto;
    
    opens nl.haltedata to spring.core, spring.context, spring.beans;
    opens nl.haltedata.gtfs to spring.core, spring.context, spring.beans;
    opens nl.haltedata.gtfs.config to spring.core, spring.context, spring.beans;
    opens nl.haltedata.gtfs.config.batch to spring.core, spring.context, spring.beans;
    opens nl.haltedata.gtfs.dto;

}