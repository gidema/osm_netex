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
    requires org.slf4j;
    requires org.locationtech.jts;
    requires java.sql;
    requires proj4j;
    requires org.apache.commons.csv;
    requires osmosis.core;
    requires osmosis.xml;
//    requires hibernate.spatial;
    
    opens nl.haltedata.chb to spring.core, spring.context, spring.beans;
    opens nl.haltedata.chb.dto;
    
    opens nl.haltedata.gtfs to spring.core, spring.context, spring.beans;
    opens nl.haltedata.gtfs.dto;

}