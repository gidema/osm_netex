package nl.haltedata.tools;

import java.io.IOException;
import java.util.Iterator;

import javax.xml.stream.XMLStreamException;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.xml.StaxEventItemReader;

import nl.haltedata.chb.mapping.ParentChildMapper;

public abstract class StaxParentChildEventItemReader<C, CT, P> implements ItemStreamReader<CT> {
    private final ParentChildMapper<C, CT, P> mapper;
    private final StaxEventItemReader<P> parentReader;
    private Iterator<C> quayIterator;
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
    }

    @Override
    public void close() throws ItemStreamException {
        parentReader.close();
    }

    @Override
    public CT read() throws IOException, XMLStreamException {
        if (quayIterator == null) {
            var hasNext = nextParent();
            if (!hasNext) return null;
        }
        while (!quayIterator.hasNext()) {
            var hasNext = nextParent();
            if (!hasNext) return null;        
        }
        var quay = quayIterator.next();
        return mapper.map(quay, parent);
    }

    protected abstract Iterator<C> getChildIterator(P p);
    
    private boolean nextParent() throws IOException {
        try {
            this.parent = parentReader.read();
            if (parent == null) return false;
            quayIterator = getChildIterator(parent);
        } catch (Exception e) {
            throw new IOException(e);
        }
        return true;
    }
}
