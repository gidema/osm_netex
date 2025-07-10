package nl.haltedata.chb;


import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import nl.haltedata.MainConfiguration;
import nl.haltedata.chb.dto.ChbDtoPackageMarker;

@SpringBootApplication
@ComponentScan(basePackages = { "nl.haltedata.chb" },
excludeFilters = { @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "nl.haltedata.chb.config.batch.*")})
@EntityScan(basePackageClasses = { ChbDtoPackageMarker.class })
@Import(MainConfiguration.class)
public class ChbOsmExporter implements ApplicationContextAware {
    @Value("${osm_netex.path.results}")
    private Path resultsPath;
    
    private static Logger LOG = LoggerFactory
      .getLogger(ChbOsmExporter.class);

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
//        new SpringApplicationBuilder(ChbOsmExporter.class)
//            .web(WebApplicationType.NONE)
//            .run();
        var instance = new ChbOsmExporter();
        instance.run();
        LOG.info("APPLICATION FINISHED");
    }

    private ApplicationContext applicationContext;
 
    public void run() {
        LOG.info("EXECUTING : command line runner");
        var jobRegistry = applicationContext.getBean(JobRegistry.class);
        var jobLauncher = applicationContext.getBean(JobLauncher.class);
        Job job;
        try {
            job = jobRegistry.getJob("exportChbToOsmJob");
            var osmFile = resultsPath.resolve("chb_quays.osm").toFile();


            jobLauncher.run(job, new JobParametersBuilder()
                    .addString("filePath", osmFile.toString())
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}