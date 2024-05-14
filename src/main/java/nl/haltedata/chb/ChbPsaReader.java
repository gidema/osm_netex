package nl.haltedata.chb;

import java.util.Iterator;

import org.springframework.batch.item.xml.StaxEventItemReader;

import nl.chb.psa.Quay;
import nl.chb.psa.Userstopcodedata;
import nl.haltedata.chb.dto.ChbPsa;
import nl.haltedata.tools.ParentChildMapper;
import nl.haltedata.tools.StaxParentChildEventItemReader;

public class ChbPsaReader extends StaxParentChildEventItemReader<Userstopcodedata, ChbPsa, Quay> {

    public ChbPsaReader(StaxEventItemReader<Quay> parentReader,
            ParentChildMapper<Userstopcodedata, ChbPsa, Quay> mapper) {
        super(parentReader, mapper);
    }

    @Override
    protected Iterator<Userstopcodedata> getChildIterator(Quay parent) {
        return parent.getUserstopcodes().getUserstopcodedata().iterator();
    }

}
