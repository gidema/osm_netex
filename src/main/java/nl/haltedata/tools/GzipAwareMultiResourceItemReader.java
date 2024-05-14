package nl.haltedata.tools;

import java.io.IOException;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.core.io.Resource;

public class GzipAwareMultiResourceItemReader<T> extends MultiResourceItemReader<T> {

    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        super.open(executionContext);
    }

    @Override
    public void setResources(Resource[] resources) {
        Resource[] wrappedResources = new Resource[resources.length];
        for (int i = 0; i < resources.length; i++) {
           var resource = resources[i];
           if (resource.getFilename() != null && resource.getFilename().endsWith(".gz")) {
               try {
                wrappedResources[i] = new GzipFileSystemResource(resource.getFile().getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
           }
           else {
               wrappedResources[i] = resource;
           }
        }
        super.setResources(wrappedResources);
    }

}
