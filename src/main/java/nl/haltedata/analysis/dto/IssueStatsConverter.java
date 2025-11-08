package nl.haltedata.analysis.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IssueStatsConverter implements AttributeConverter<Map<String, Integer>, String> {
    private static final JsonMapper objectMapper = new JsonMapper();
    private static final TypeReference<Map.Entry<String, Integer>[]> ref = new TypeReference<>() {/**/};

    @Override
    public String convertToDatabaseColumn(Map<String, Integer> stats) {
        try {
            var entries = new ArrayList<Map.Entry<String, Integer>>(stats.size());
            stats.entrySet().forEach(entries::add);
            return objectMapper.writeValueAsString(entries);
        } catch (JsonProcessingException jpe) {
//            log.warn("Cannot convert Address into JSON");
            return null;
        }
    }

    @Override
    public Map<String, Integer> convertToEntityAttribute(String value) {
        if (value == null) return Collections.emptyMap();
        try {
            var map = new HashMap<String, Integer>();
            Map.Entry<String, Integer>[] entries = objectMapper.readValue(value, ref);
            for(var entry : entries) {
                map.put(entry.getKey(), entry.getValue());
            }
            return map;
        } catch (JsonProcessingException e) {
//            log.warn("Cannot convert JSON into Address");
            return null;
        }
    }
}
