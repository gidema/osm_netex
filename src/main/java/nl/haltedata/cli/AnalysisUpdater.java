package nl.haltedata.cli;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;

@Component
public class AnalysisUpdater implements ApplicationRunner {

    @Inject JobRegistry jobRegistry;
    @Inject JobLauncher jobLauncher;
    
    private static Logger LOG = LoggerFactory
      .getLogger(AnalysisUpdater.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("EXECUTING : command line runner");
        Job job;
        try {
            job = jobRegistry.getJob("comparisonEtlUpdate");
            jobLauncher.run(job, new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}