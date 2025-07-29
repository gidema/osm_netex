package nl.haltedata.analysis.etl;

import static java.util.Map.entry;

import java.util.Map;

/**
 * 
 */
public class ColourMap {
    public final static Map<String, String> colourMap = Map.ofEntries(
            entry("WHITE", "#FFFFFF"),
            entry("LIGHT_GRAY", "#C0C0C0"),
            entry("GRAY", "#808080"),
            entry("DARK_GRAY", "#404040"),
            entry("BLACK", "#000000"),
            entry("RED", "#FF0000"),
            entry("PINK", "#FFAFAF"),
            entry("ORANGE", "#FFC800"),
            entry("YELLOW", "#FFFF00"),
            entry("GREEN", "#00FF00"),
            entry("MAGENTA", "#FF00FF"),
            entry("CYAN", "#00FFFF"),
            entry("BLUE", "#0000FF"));
    
    /**
     * Normalize a color value to the hexadecimal format in upper case.
     * 
     * @param colour the color to normalize
     * @return The normalized color
     */
    public static String normalizeColour(String colour) {
        if (colour == null) return null;
        return colourMap.getOrDefault(colour.toUpperCase(), colour.toUpperCase());
    }


}
