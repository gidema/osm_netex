package nl.haltedata.core.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import nl.haltedata.chb.dto.DimChbQuay;
import nl.haltedata.chb.dto.DimChbQuayRepository;

@Service
public class BackendChbQuayService implements ChbQuayService {
    @Inject
    private DimChbQuayRepository chbQuayRepository;
    
    private final Map<String, DimChbQuay> quayCache = new HashMap<>();
    
    @Override
    public void fetchMissingCodes(Set<String> quayCodes) {
        var missingQuayCodes = quayCodes.stream().filter(code -> !quayCache.containsKey(code)).collect(Collectors.toSet());
        if (!missingQuayCodes.isEmpty()) {
            fetchMissingObjects(missingQuayCodes);
        }
    }
    
    private void fetchMissingObjects(Set<String> quayCodes) {
        chbQuayRepository.findAllByQuayCodeIn(quayCodes)
            .forEach(chbQuay -> quayCache.put(chbQuay.getQuayCode(), chbQuay));
    }
    
    @Override
    public DimChbQuay getQuay(String quayCode) {
        return quayCache.get(quayCode);
    }
}
