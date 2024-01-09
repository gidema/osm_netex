/**
 * 
 */
/**
 * 
 */
module CHBHalteData {
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
    
    opens nl.haltedata.chb to spring.core, spring.context, spring.beans;
    opens nl.haltedata.chb.dto;
}