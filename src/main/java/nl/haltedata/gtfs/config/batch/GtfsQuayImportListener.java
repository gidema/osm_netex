package nl.haltedata.gtfs.config.batch;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.Chunk;

import nl.haltedata.gtfs.dto.GtfsQuay;

public class GtfsQuayImportListener implements ItemWriteListener<GtfsQuay>, JobExecutionListener{

//    private SimpMessagingTemplate simpMessagingTemplate;
    private String fileName;
    private AtomicInteger runningWriteCount = new AtomicInteger(0);
    private JobExecution jobExecution;

    @Override
    public void beforeWrite(Chunk<? extends GtfsQuay> items) {
        //
    }

    @Override
    public void afterWrite(Chunk<? extends GtfsQuay> items) {
        this.runningWriteCount.addAndGet(items.size());
//        double percentageComplete = (writeCount / recordCount) * 100;
        var stepExecution = jobExecution.getStepExecutions().iterator().next();
        stepExecution.setStatus(BatchStatus.STARTED);
        stepExecution.setWriteCount(runningWriteCount.longValue());
//        jobProgressMessage.setPercentageComplete(percentageComplete);
//        jobProgressMessage.setFileName(fileName);
//        simpMessagingTemplate.convertAndSend("/topic/public", jobProgressMessage);
    }

    @Override
    public void onWriteError(Exception exception, Chunk<? extends GtfsQuay> items) {
        //
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void beforeJob(@SuppressWarnings("hiding") JobExecution jobExecution) {
        this.jobExecution = jobExecution;
//        String absoluteFilePath = jobExecution.getJobParameters().getString("absoluteFileName");
//        try {
//            recordCount = countLines(absoluteFilePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void afterJob(@SuppressWarnings("hiding") JobExecution jobExecution) {
        var stepExecution = jobExecution.getStepExecutions().iterator().next();
        stepExecution.setStatus(BatchStatus.COMPLETED);

    }

    //https://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
    public static int countLines(String filename) throws IOException {
        try (
            InputStream is = new BufferedInputStream(new FileInputStream(filename));
        )
        {
            byte[] c = new byte[1024];

            int readChars = is.read(c);
            if (readChars == -1) {
                // bail out if nothing to read
                return 0;
            }

            // make it easy for the optimizer to tune this loop
            int count = 0;
            while (readChars == 1024) {
                for (int i=0; i<1024;) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            // count remaining characters
            while (readChars != -1) {
                System.out.println(readChars);
                for (int i=0; i<readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            return count == 0 ? 1 : count;
        }
    }
}