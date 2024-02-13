package nl.haltedata.gtfs.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map a gtfs stopcode to an IFOPT reference.
 * In most situations, this is a one-on-one mapping, but there are several exception.
 * For Amsterdam and Den Haag, the gtfs stop codes have less than 8 positions. They get
 * a prefix (Amsterdam 300., Den Haag 320..). Stop codes in the Rotterdam area have a 'HA' 
 * prefix. This is replaced with a 310... prefix;
 */
public class RefIfoptFactory {
    private static Map<String, List<String>> templatePlaces = Map.of(
        "30000000", List.of("Amsterdam", "Amsterdam-Duivendrecht", "Duivendrecht", "Diemen",
                "Velsen", "Zaandam", "Amstelveen", "Weesp", "Schiphol"),
        "31000000", List.of("Rotterdam"),
        "32000000", List.of("Den Haag", "Rijswijk", "Leidschendam", "'s-Gravenhage",
                "Voorburg", "Zoetermeer", "Nootdorp", "Delft", "Lansingerland",
                "Wateringen"));

    private static Map<String, String> templateMap = buildTemplateMap();
    
    public static String createIfopt(GtfsQuay quay) {
        String ifoptCode = quay.getQuayCode();
        String template = null;
        if (ifoptCode.startsWith("HA")) {
            ifoptCode = ifoptCode.replace("HA", "");
            template = "31000000";
        }    
        if (ifoptCode.length() < 8) {
            if (template == null) {
                template = templateMap.get(quay.getPlace());
            }
            if (template != null) {
                ifoptCode = template.substring(0, 8 - ifoptCode.length()) + ifoptCode;
            }
        }
        return ifoptCode;
    }
    
    private static Map<String, String> buildTemplateMap() {
        Map<String, String> map = new HashMap<>();
        templatePlaces.forEach((template, places) -> {
            places.forEach(place -> map.put(place, template));
        });
        return map;
    }
}
