package nl.haltedata.netex.config;

import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfiguration {
    @Bean
    static GeometryFactory geometryFactory() {
        return new GeometryFactory();
    }
}
