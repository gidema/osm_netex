package nl.haltedata.validation;

import java.nio.file.InvalidPathException;
import java.text.MessageFormat;
import java.util.Arrays;

public class MockI18n implements I18n {

    @Override
    public String tr(String string, Object... parameters) {
        return format(string, parameters);
    }

    private static String format(String text, Object... objects) {
        if (objects.length == 0 && !text.contains("'")) {
            return text;
        }
        try {
            return MessageFormat.format(text, objects);
        } catch (InvalidPathException e) {
            System.err.println("!!! Unable to format '" + text + "': " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
