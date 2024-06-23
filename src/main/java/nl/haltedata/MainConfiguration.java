package nl.haltedata;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import nl.haltedata.netex.ndov.NdovService;
import nl.haltedata.tools.RdToWgs84Transformation;

@Configuration
public class MainConfiguration {
    @SuppressWarnings("static-method")
    @Bean
    GeometryFactory wsgGeometryFactory() {
        return new GeometryFactory(new PrecisionModel(), 4326);
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

//    @SuppressWarnings("static-method")
//    @Bean
//    JobRegistry jobRegistry() {
//        return new MapJobRegistry();
//    }
//
//    @SuppressWarnings("static-method")
//    @Bean
//    TaskExecutor taskExecutor() {
//        return new SyncTaskExecutor();
//    }
//    
//    @SuppressWarnings("static-method")
//    @Bean
//    JobLauncher jobLauncher() {
//        return new TaskExecutorJobLauncher();
//    }
    
   @SuppressWarnings("static-method")
    @Bean
    NdovService ndovService() {
        return new NdovService();
    }
}
