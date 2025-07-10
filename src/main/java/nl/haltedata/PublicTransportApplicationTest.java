package nl.haltedata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = { "nl.haltedata.chb", "nl.haltedata.gtfs", "nl.haltedata.netex", "nl.haltedata.analysis", "nl.haltedata.osm", "nl.haltedata.validation"},
    excludeFilters = { @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.gtfs.config.batch.*"),
        @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.netex.config.batch.*"),
        @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.chb.config.batch.*"),
        @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.osm.config.batch.*")})
@EntityScan({"nl.haltedata.analysis.dto", "nl.haltedata.netex.dto", "nl.haltedata.osm.dto", "nl.haltedata.chb.dto", "nl.haltedata.gtfs.dto", "nl.haltedata.validation.model"})
//@ComponentScan(basePackages = { "nl.haltedata"}, excludeFilters = { @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.gtfs.config.batch.*"),
//    @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.netex.config.batch.*"),
//    @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.chb.config.batch.*"),
//    @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.osm.config.batch.*")})
@Import(MainConfiguration.class)
public class PublicTransportApplicationTest {
    private static Logger LOG = LoggerFactory
    	      .getLogger(PublicTransportApplicationTest.class);

    @SuppressWarnings("resource")
	public static void main(String[] args) {
    	var application = new SpringApplication(PublicTransportApplicationTest.class);

        LOG.info("STARTING THE APPLICATION");
        application.run(args);
    }
}
