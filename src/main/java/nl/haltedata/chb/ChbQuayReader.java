package nl.haltedata.chb;

import java.util.Iterator;

import org.springframework.batch.item.xml.StaxEventItemReader;

import nl.chb.Quay;
import nl.chb.Stopplace;
import nl.haltedata.chb.dto.ChbQuay;
import nl.haltedata.chb.mapping.ParentChildMapper;
import nl.haltedata.tools.StaxParentChildEventItemReader;

public class ChbQuayReader extends StaxParentChildEventItemReader<Quay, ChbQuay, Stopplace> {

    public ChbQuayReader(StaxEventItemReader<Stopplace> parentReader,
            ParentChildMapper<Quay, ChbQuay, Stopplace> mapper) {
        super(parentReader, mapper);
    }

    @Override
    protected Iterator<Quay> getChildIterator(Stopplace parent) {
        return parent.getQuays().getQuay().iterator();
    }

}
