package nl.haltedata.gtfs.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nl.haltedata.gtfs.dto.GtfsSpecialQuayRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/importSpecials")
    public class ImportSpecialsController {
        private static String file="/home/gertjan/projects/NLGeo/Haltedata/gtfs_special_stops.csv";
    
        private final JobLauncher jobLauncher;
        private final JobRegistry jobRegistry;
        private final JobExplorer jobExplorer;
        private final GtfsSpecialQuayRepository quayRepository;

        /**
         * Endpoint to start the contacts import batch job.
         * Simulates a user uploading a CSV file of contacts.
         *
         * @return Response indicating if the batch job was invoked successfully.
         * @throws Exception if any error occurs during job launch.
         */
        @GetMapping("/start")
        public ResponseEntity<String> handle() throws Exception {

            // simulate the user uploading a CSV file of contacts to this controller endpoint
            FileSystemResource data = new FileSystemResource(file);
            String pathToResource = data.getFile().getAbsolutePath();

            JobParameters params = new JobParametersBuilder()
                .addString("filePath", pathToResource)
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
            var job = jobRegistry.getJob("gtfsSpecialQuayImportJob");
            jobLauncher.run(job, params);

            return ResponseEntity.ok().body("Batch job has been invoked");
        }

        /**
         * Endpoint to fetch the current status of the contacts import batch job.
         * Provides insights like job status, number of records read/written, progress percentage, etc.
         * Also verifies the number of records in the Contacts table by calling our repositories count() function
         *
         * @return Response with status and metrics related to the batch job.
         */
        @GetMapping("/status")
        public ResponseEntity<Map<String, Object>> getJobStatus() {
            Map<String, Object> response = new HashMap<>();

            List<JobInstance> instances = jobExplorer.getJobInstances("importContactsJob", 0, 1);
            if (instances.isEmpty()) {
                response.put("message", "No job instance found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(instances.get(0));

            if (jobExecutions.isEmpty()) {
                response.put("message", "No job execution found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            JobExecution lastJobExecution = jobExecutions.get(0);
            for (JobExecution jobExecution : jobExecutions) {
                if (jobExecution.getCreateTime().isAfter(lastJobExecution.getCreateTime())) {
                    lastJobExecution = jobExecution;
                }
            }

            BatchStatus batchStatus = lastJobExecution.getStatus();
            response.put("status", batchStatus.toString());

            Collection<StepExecution> stepExecutions = lastJobExecution.getStepExecutions();
            for (StepExecution stepExecution : stepExecutions) {
                // In our case, there's only one step. If you have multiple steps, you might want to key by step name.
                response.put("readCount", stepExecution.getReadCount());
                response.put("writeCount", stepExecution.getWriteCount());
                response.put("commitCount", stepExecution.getCommitCount());
                response.put("skipCount", stepExecution.getSkipCount());
                response.put("rollbackCount", stepExecution.getRollbackCount());
                response.put("recordsInDB", quayRepository.count());

                // Progress indicator. Assuming you know the total records in advance (100,000 in this case).
                int progress = (int) (((double) stepExecution.getReadCount() / 100000) * 100);
                response.put("progress", progress + "%");
            }

            return ResponseEntity.ok().body(response);
        }

    }