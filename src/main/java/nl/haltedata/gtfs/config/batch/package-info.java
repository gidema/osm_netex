/*
 * Package for the Spring Batch configuration files.
 * Prevent bean naming conflicts by putting the batch configuration files for batches is a
 * separate package that is not a descendant of the package containing the parent configuration.
 * 
 * The parent configuration class should be annotated with @EnableBatchProcessing(modular = true) 
 * and have GenericApplicationContextFactory beans for each batch configuration
 */
package nl.haltedata.gtfs.config.batch;

