package nl.haltedata.chb.mapping;

import nl.chb.Stopplace;
import nl.haltedata.chb.dto.ChbStopPlace;
import nl.haltedata.tools.ParentChildMapper;

public class StopPlaceMapper implements ParentChildMapper<Stopplace, ChbStopPlace, Void> {
    private ChbStopPlaceProcessor processor = new ChbStopPlaceProcessor();

    @Override
    public ChbStopPlace map(Stopplace stopplace, Void dummy) throws Exception {
        return processor.process(stopplace);
    }
}
