package nl.haltedata.netex.config;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nl.haltedata.tools.RdToWgs84Transformation;

@Configuration
public class MainConfiguration {
    @SuppressWarnings("static-method")
    @Bean
    GeometryFactory geometryFactory() {
        return new GeometryFactory();
    }
    
    @SuppressWarnings("static-method")
    @Bean
    GeometryFactory rdGeometryFactory() {
        return new GeometryFactory(new PrecisionModel(), 28992);
    }
    
    @SuppressWarnings("static-method")
    @Bean
    RdToWgs84Transformation transformation() {
        return new RdToWgs84Transformation();
    }

}
