package nl.haltedata.chb.controllers;

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
    @RequestMapping("/chb/exportOsm")
    @RequiredArgsConstructor
    public class ExportChbToOsmController {
        private static String file="/home/gertjan/Downloads/chb_quays.osm";
    
        private final JobLauncher jobLauncher;
        private final JobRegistry jobRegistry;

        /**
         * Endpoint to start the quays export batch job.
         *
         * @return Response indicating if the batch job was invoked successfully.
         * @throws Exception if any error occurs during job launch.
         */
        @GetMapping("/start")
        public ResponseEntity<String> handle() throws Exception {
            JobParameters params = new JobParametersBuilder()
                .addString("filePath", file)
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
            var job = jobRegistry.getJob("exportChbToOsmJob");
            jobLauncher.run(job, params);

            return ResponseEntity.ok().body("Batch job has been invoked");
        }
    }