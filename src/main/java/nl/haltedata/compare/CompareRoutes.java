package nl.haltedata.compare;

import java.util.List;
import static java.lang.String.format;

import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.haltedata.netex.dto.NetexRouteData;
import nl.haltedata.netex.dto.NetexRouteDataRepository;
import nl.haltedata.osm.dto.OsmRouteData;
import nl.haltedata.osm.dto.OsmRouteDataRepository;

@Component
public class CompareRoutes {
    @Inject
    NetexRouteDataRepository netexRepository;
    @Inject
    OsmRouteDataRepository osmRepository;
    
    public String compare() {
        Long osmRouteId = 446201L;
        return osmRepository.findById(osmRouteId).map(this::compare).orElse(
            format("No osm route with id %d could be found.", osmRouteId));
    }
    
    private String compare(OsmRouteData osmData) {
        List<NetexRouteData> netexDataList = netexRepository.findByEndpoints(osmData.getLineNumber(), osmData.getStartStopplaceCode(),
                osmData.getEndStopplaceCode(), osmData.getQuayCount() - 2,  osmData.getQuayCount() + 2);
            if (netexDataList.isEmpty()) {
                return format("No matching NeTeX route was found for the osm route with id %d;\n", osmData.getOsmRouteId());
            }
            if (netexDataList.size() > 1) {
                return format("There is more than 1 matching NeTeX route for the osm route with id %d;\n", osmData.getOsmRouteId());
            }
            return compare(osmData, netexDataList.get(0));

    }
    
    private String compare(OsmRouteData osmData, NetexRouteData netexData) {
        // TODO Auto-generated method stub
        return null;
    }

}
