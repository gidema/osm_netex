package nl.haltedata.gtfs.mapping;

import org.apache.commons.csv.CSVRecord;

import nl.haltedata.gtfs.dto.GtfsStopTime;
import nl.haltedata.tools.CsvMapper;

public class StopTimeMapper implements CsvMapper<GtfsStopTime> {

    @Override
    public GtfsStopTime map(CSVRecord rec) {
        var st = new GtfsStopTime();
        st.setTripId(getLong(rec, 0));
        st.setStopSequence(getInteger(rec, 1));
        st.setStopId(getLong(rec, 2));
        st.setStopHeadsign(getString(rec, 3));
        st.setArrival_time(getString(rec, 4));
        st.setDeparture_time(getString(rec, 5));
        st.setPickup_type(getInteger(rec, 6));
        st.setDrop_off_type(getInteger(rec, 7));
        st.setTimepoint(getInteger(rec, 8));
        st.setShape_dist_traveled(getDouble(rec, 9));
        st.setFare_units_traveled(getDouble(rec, 10));
        return st;
    }
}
