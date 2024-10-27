package nl.haltedata.check.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import nl.haltedata.check.model.DimChbQuay;

public class ChbQuayService {
    private final static ChbQuayService INSTANCE = new ChbQuayService();
    
    private final RestClient restClient = RestClient.create();
    private final Map<String, DimChbQuay> quayCache = new HashMap<>();
    
    public static ChbQuayService getInstance() {
        return INSTANCE;
    }
    
    private ChbQuayService() {
        // Empty private constructor for singleton class
    }
    
    public void fetchMissingCodes(Set<String> quayCodes) {
        var missingQuayCodes = quayCodes.stream().filter(code -> !quayCache.containsKey(code)).collect(Collectors.toSet());
        if (!missingQuayCodes.isEmpty()) {
            fetchMissingObjects(missingQuayCodes);
        }
    }
    
    private void fetchMissingObjects(Set<String> quayCodes) {
        var codeList =String.join(",", quayCodes);
        List<DimChbQuay> chbQuays = restClient.get()
            .uri("http://localhost:8080/chb/quay/" + codeList)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {/** */});
        chbQuays.forEach(chbQuay -> quayCache.put(chbQuay.getQuayCode(), chbQuay));
    }
    
    public DimChbQuay getQuay(String quayCode) {
        return quayCache.get(quayCode);
    }
}
