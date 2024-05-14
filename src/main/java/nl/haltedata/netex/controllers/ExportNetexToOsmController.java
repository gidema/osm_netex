package nl.haltedata.netex.controllers;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
    @RequestMapping("/netex/exportOsm")
    @RequiredArgsConstructor
    public class ExportNetexToOsmController {
        private static String file="/home/gertjan/Downloads/netex_quays.osm";
    
        private final JobLauncher jobLauncher;
        private final JobRegistry jobRegistry;
//        private final JobExplorer jobExplorer;
//        private final GtfsQuayRepository quayRepository;

        /**
         * Endpoint to start the contacts import batch job.
         * Simulates a user uploading a CSV file of contacts.
         *
         * @return Response indicating if the batch job was invoked successfully.
         * @throws Exception if any error occurs during job launch.
         */
        @GetMapping("/start")
        public ResponseEntity<String> handle() throws Exception {

//            // simulate the user uploading a CSV file of contacts to this controller endpoint
//            FileSystemResource sampleContactsData = new FileSystemResource(file);
//            String pathToResource = sampleContactsData.getFile().getAbsolutePath();

            JobParameters params = new JobParametersBuilder()
                .addString("filePath", file)
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
            var job = jobRegistry.getJob("exportNetexToOsmJob");
            jobLauncher.run(job, params);

            return ResponseEntity.ok().body("Batch job has been invoked");
        }
    }