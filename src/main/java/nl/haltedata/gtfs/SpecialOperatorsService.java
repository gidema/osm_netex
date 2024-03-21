package nl.haltedata.gtfs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import nl.haltedata.gtfs.dto.GtfsSpecialQuayRepository;

@Service
public class SpecialOperatorsService {
    private Map<Long, String> operators = new HashMap<>();
    private boolean initialized = false;

    @Inject
    private GtfsSpecialQuayRepository specialQuayRepository;

    public String getOperator(Long gtsfStopId) {
        if (!initialized) {
            initialize();
        }
        return operators.get(gtsfStopId);
    }
    
    private void initialize() {
        specialQuayRepository.findAll().forEach(quay -> {
            if (quay.getGtfsId() != null) {
                operators.put(quay.getGtfsId(), quay.getOperator());
            }
        });
        initialized = true;
    }
}
