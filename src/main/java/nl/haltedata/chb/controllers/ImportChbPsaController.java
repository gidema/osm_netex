package nl.haltedata.chb.controllers;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nl.haltedata.gtfs.ImportStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/importChbPsa")
public class ImportChbPsaController {
    private static Path psaFile = Path.of("/home/gertjan/projects/NLGeo/Haltedata/chb/PassengerStopAssignmentExportCHB20240414013035_8.1.1.xml.gz"); 
    private static String JOB_NAME = "chbPsaImportJob";

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final JobExplorer jobExplorer;


    /**
     * Endpoint to start the import batch job.
     *
     * @return Response indicating if the batch job was invoked successfully.
     * @throws Exception if any error occurs during job launch.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/start")
    public ResponseEntity<String> handle() throws Exception {
        JobParameters params = new JobParametersBuilder().addString("filePath", psaFile.toString())
                .addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();
        var job = jobRegistry.getJob(JOB_NAME);
        jobLauncher.run(job, params);

        return ResponseEntity.ok().body("Batch job has been invoked");
    }

    /**
     * Endpoint to fetch the current status of the import batch job.
     * Provides insights like job status, number of records read/written, progress
     * percentage, etc. Also verifies the number of records in the database table by
     * calling our repositories count() function
     *
     * @return Response with status and metrics related to the batch job.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/status")
    public ResponseEntity<ImportStatus> getJobStatus() {
        var response = new ImportStatus();

        List<JobInstance> instances = jobExplorer.getJobInstances(JOB_NAME, 0, 1);
        if (instances.isEmpty()) {
//            response.put("message", "No job instance found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(instances.get(0));

        if (jobExecutions.isEmpty()) {
//            response.put("message", "No job execution found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        JobExecution lastJobExecution = jobExecutions.get(0);
        for (JobExecution jobExecution : jobExecutions) {
            if (jobExecution.getCreateTime().isAfter(lastJobExecution.getCreateTime())) {
                lastJobExecution = jobExecution;
            }
        }

        BatchStatus batchStatus = lastJobExecution.getStatus();
        response.setStatus(batchStatus.toString());

        Collection<StepExecution> stepExecutions = lastJobExecution.getStepExecutions();
        for (StepExecution stepExecution : stepExecutions) {
            // In our case, there's only one step. If you have multiple steps, you might
            // want to key by step name.
            response.setReadCount(stepExecution.getReadCount());
            response.setWriteCount(stepExecution.getWriteCount());
            response.setCommitCount(stepExecution.getCommitCount());
            response.setSkipCount(stepExecution.getSkipCount());
            response.setRollbackCount(stepExecution.getRollbackCount());
//            response.put("quaysInDB", quayRepository.count());

            // Progress indicator. Assuming you know the total records in advance (100,000
            // in this case).
            Double progress = (((double) stepExecution.getReadCount() / 75000));
            response.setProgress(progress);
        }

        return ResponseEntity.ok().body(response);
    }
}