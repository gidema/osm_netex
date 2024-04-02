package nl.haltedata.netex.ndov;

import java.io.IOException;
import java.util.Iterator;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;

import nl.haltedata.netex.dto.NetexFileInfo;

public class NdovNetexFileInfoReader implements ItemReader<NetexFileInfo>, ItemStream {
    private NdovNetexService service;
    private Iterator<String> folderIterator;
    private Iterator<NetexFileInfo> fileInfoIterator;
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        ItemStream.super.open(executionContext);
        this.service = new NdovNetexService();
        this.folderIterator = service.getAgencyFolders().iterator();
        try {
            nextFolder();
        } catch (IOException e) {
            throw new ItemStreamException(e);
        }
    }

    @Override
    public NetexFileInfo read() throws Exception {
        while (!fileInfoIterator.hasNext() && folderIterator.hasNext()) {
            nextFolder();
        }
        if (fileInfoIterator.hasNext()) return fileInfoIterator.next();
        return null;
    }

    private void nextFolder() throws IOException {
        if (folderIterator.hasNext()) {
            var folder = folderIterator.next();
            this.fileInfoIterator = service.getFileInfo(folder).iterator();
        }
    }

    @Override
    public void close() throws ItemStreamException {
        if (service != null)
            try {
                service.close();
            } catch (IOException e) {
                throw new ItemStreamException(e);
            }
        ItemStream.super.close();
    }
    
    
}
