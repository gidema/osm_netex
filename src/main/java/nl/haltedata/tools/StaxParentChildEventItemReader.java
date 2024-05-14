package nl.haltedata.tools;

import java.io.IOException;
import java.util.Iterator;

import javax.xml.stream.XMLStreamException;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.Resource;

public abstract class StaxParentChildEventItemReader<C, CT, P> implements ResourceAwareItemReaderItemStream<CT> {
    private final ParentChildMapper<C, CT, P> mapper;
    private final StaxEventItemReader<P> parentReader;
    private Iterator<C> childIterator;
    private P parent;
    
    public StaxParentChildEventItemReader(StaxEventItemReader<P> parentReader,
            ParentChildMapper<C, CT, P> mapper) {
        super();
        this.parentReader = parentReader;
        this.mapper = mapper;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        parentReader.open(executionContext);
        // Save the execution context.
        // We need it to open the parent reader, once the resource is set.
    }

    @Override
    public void close() throws ItemStreamException {
        parentReader.close();
    }

    @Override
    public CT read() throws IOException, XMLStreamException {
        if (childIterator == null) {
            var hasNext = nextParent();
            if (!hasNext) return null;
        }
        while (!childIterator.hasNext()) {
            var hasNext = nextParent();
            if (!hasNext) return null;        
        }
        var quay = childIterator.next();
        try {
            return mapper.map(quay, parent);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void setResource(Resource resource) {
        parentReader.setResource(resource);
    }

    protected abstract Iterator<C> getChildIterator(P p);
    
    private boolean nextParent() throws IOException {
        try {
            this.parent = parentReader.read();
            if (parent == null) return false;
            childIterator = getChildIterator(parent);
        } catch (Exception e) {
            throw new IOException(e);
        }
        return true;
    }
}
