package nl.haltedata.util;

import java.nio.file.InvalidPathException;
import java.text.MessageFormat;

public class MockI18n implements I18n {

    @Override
    public String tr(String string, String... parameters) {
        return format(string, parameters);
    }

    private static String format(String text, String... objects) {
        if (objects.length == 0 && !text.contains("'")) {
            return text;
        }
        try {
            return MessageFormat.format(text, (Object[])objects);
        } catch (InvalidPathException e) {
            System.err.println("!!! Unable to format '" + text + "': " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
