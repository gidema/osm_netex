package nl.haltedata.gtfs.mapping;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.boot.context.properties.bind.BindException;

import nl.haltedata.gtfs.dto.GtfsSpecialQuay;

public class SpecialQuayFieldSetMapper implements FieldSetMapper<GtfsSpecialQuay> {
 
    @Override
    public GtfsSpecialQuay mapFieldSet(FieldSet fieldSet) throws BindException {
        var quay = new GtfsSpecialQuay();
        quay.setGtfsId(fieldSet.readLong(0));
        quay.setOperator(fieldSet.readString(1));
        return quay;
    }
}
