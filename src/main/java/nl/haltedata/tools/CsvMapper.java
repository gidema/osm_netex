package nl.haltedata.tools;

import java.time.LocalTime;

import org.apache.commons.csv.CSVRecord;

public interface CsvMapper<T> {
    public T map(CSVRecord rec);
    
    public default Integer getInteger(CSVRecord rec, int column) {
        var value = rec.get(column);
        return ((value == null || value.isEmpty()) ? null : Integer.valueOf(rec.get(column)));
    }
    
    public default Long getLong(CSVRecord rec, int column) {
        var value = rec.get(column);
        return ((value == null || value.isEmpty()) ? null : Long.valueOf(rec.get(column)));
    }
    
    public default String getString(CSVRecord rec, int column) {
        var value = rec.get(column);
        return ((value == null || value.isEmpty()) ? null : value);
    }
    
    public default Double getDouble(CSVRecord rec, int column) {
        var value = rec.get(column);
        return ((value == null || value.isEmpty()) ? null : Double.valueOf(rec.get(column)));
    }
    
    public default LocalTime getLocalTime(CSVRecord rec, int column) {
        var value = rec.get(column);
        return (value.isBlank() ? null : LocalTime.parse(value));
    }
}
