package nl.haltedata.netex.config.batch;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.AbstractStep;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import nl.haltedata.netex.dto.NetexFileInfo;
import nl.haltedata.netex.dto.NetexFileRepository;
import nl.haltedata.netex.dto.NetexTransportArea;
import nl.haltedata.netex.dto.NetexTransportAreaRepository;
import nl.haltedata.netex.ndov.NdovNetexFileInfoReader;
import nl.haltedata.netex.ndov.NdovNetexService;
import nl.haltedata.tools.ExecuteSqlTasklet;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class BatchImportConfigForNetexFileInfo {
    private static Path FILE_PATH = Path.of("/home/gertjan/.public_transport/netex");

    private static String clearTableNetexFileInfo = """
TRUNCATE TABLE netex.netex_file_info;
""";
    
    private final EntityManagerFactory entityManagerFactory;

    public List<String> fileGroupNames;

    private ListItemWriter<NetexFileInfo> fileInfoWriter;

    /**
     * Creates and returns a {@link ItemReader} bean for NetexFileInfo entities.
     *
     * @return a configured ItemReader.
     */
    @Bean
    static
    NdovNetexFileInfoReader reader() {
        return new NdovNetexFileInfoReader();
    }
    
    /**
     * Defines the main batch job for importing.
     *
     * @param jobRepository the repository for storing job metadata.
     * @param truncateStep the step associated with this job.
     * @return a configured Job for exporting quays.
     */
    @SuppressWarnings("static-method")
    @Bean
    Job importJob(JobRepository jobRepository, Step readTransportAreasStep, Step readFileInfoStep,
            Step downloadStep)  {
        return new JobBuilder("importNetexFileInfoJob", jobRepository)
            .start(readTransportAreasStep)
            .next(readFileInfoStep)
            .next(downloadStep)
            .build();
    }

    @Bean
    Step readTransportAreasStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("readTransportAreaStep", jobRepository)
        .tasklet(readTransportAreas(), transactionManager)
        .allowStartIfComplete(true)
        .build();
    }

    /**
     * Defines the batch step to get the NeTex file information.
     *
     * @param jobRepository the repository for storing job metadata.
     * @param transactionManager the transaction manager to handle transactional behavior.
     * @return a configured Step for reading and writing Contact entities.
     */
    @Bean
    Step readFileInfoStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            RepositoryItemReader<NetexFileInfo> fileInforeader) {
        return new StepBuilder("readFileInfoStep", jobRepository)
            .<NetexFileInfo, NetexFileInfo>chunk(1000, transactionManager)
            .reader(fileInforeader)
            .writer(fileInfoWriter())
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    Step downloadStep() {
        return new DownloadStep();
    }


    @SuppressWarnings("static-method")
    @Bean
    RepositoryItemReader<NetexFileInfo> fileInforeader(NetexFileRepository repository) {
        return new RepositoryItemReaderBuilder<NetexFileInfo>()
                .sorts(Map.of("fileName", Direction.ASC))
                .methodName("findAll")
                .repository(repository)
                .pageSize(500)
                .saveState(false)
                .build();
    }
    
    @Bean
    ListItemWriter<NetexFileInfo> fileInfoWriter() {
        fileInfoWriter = new ListItemWriter<>();
        return fileInfoWriter;
    }
    
    @Bean
    Tasklet prepareFileInfo(TransactionTemplate transactionTemplate) {
        return new ExecuteSqlTasklet(entityManagerFactory, transactionTemplate, clearTableNetexFileInfo);
    }
    
    @Bean
    ReadTransportAreasTasklet readTransportAreas() {
        return new ReadTransportAreasTasklet();
    }
    
    class ReadTransportAreasTasklet implements Tasklet {
        @Inject
        private NetexTransportAreaRepository repository;
        
        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            fileGroupNames = StreamSupport.stream(repository.findAll().spliterator(), false)
            .map(NetexTransportArea::getTransportArea)
            .collect(Collectors.toList());
            return RepeatStatus.FINISHED;
        }
        
    }

    class DownloadStep extends AbstractStep {

        public DownloadStep() {
            this.setName("downloadStep");
            this.setAllowStartIfComplete(true);
        }

        @Override
        @Inject
        public void setJobRepository(JobRepository jobRepository) {
            super.setJobRepository(jobRepository);
        }
        
        @Override
        protected void doExecute(StepExecution stepExecution) throws Exception {
            Map<String, NetexFileInfo> latestFiles = new HashMap<>();
            fileInfoWriter.getWrittenItems().forEach(fileInfo -> {
                var fileGroup = fileInfo.getFileGroup();
                if (fileGroupNames.contains(fileGroup)) {
                    var latestFileInfo = latestFiles.get(fileGroup);
                    if (latestFileInfo == null) {
                        latestFiles.put(fileGroup, fileInfo);
                    }
                    else if (fileInfo.getFileName().compareTo(latestFileInfo.getFileName()) > 0)  {
                        latestFiles.put(fileGroup, fileInfo);                        
                    }
                }
                
            });
            downloadFiles(latestFiles.values());
            stepExecution.setExitStatus(ExitStatus.COMPLETED);
            stepExecution.setEndTime(LocalDateTime.now());
        }

        private void downloadFiles(Collection<NetexFileInfo> latestFiles) throws IOException {
            try (
                NdovNetexService service = new NdovNetexService();
            ) {
                latestFiles.forEach(fileInfo -> {
                    try {
                        var fileName = fileInfo.getFileName();
                        service.downloadFile(fileInfo.getAgencyId(), fileName, FILE_PATH.resolve(fileName));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
            }
        }
        
    }
}