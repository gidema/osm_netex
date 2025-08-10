package nl.haltedata.osm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;

@Component
public class OsmUpdater {

    private static Logger LOG = LoggerFactory
      .getLogger(OsmUpdater.class);

    @Inject JobRegistry jobRegistry;
    @Inject JobLauncher jobLauncher;
    
    public void run() {
        LOG.info("EXECUTING : OSM updater");
        Job job;
        try {
            job = jobRegistry.getJob("osmEtlUpdate");
            jobLauncher.run(job, new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}