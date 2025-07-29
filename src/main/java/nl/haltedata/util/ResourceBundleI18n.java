package nl.haltedata.util;

import java.nio.file.InvalidPathException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceBundleI18n implements I18n {
    private final ResourceBundle resources;

    public ResourceBundleI18n(Locale locale) {
        super();
        this.resources = ResourceBundle.getBundle("nl.haltedata.analysis.Analysis", locale);
    }

    @Override
    public String tr(String text, String... parameters) {
        String i18nText;
        try {
            i18nText = resources.getString(text);
        } catch (MissingResourceException e) {
            i18nText = text;
        }
//        if (parameters.length == 0 && !text.contains("'")) {
        if (parameters.length == 0) {
            return i18nText;
        }
        try {
            return MessageFormat.format(i18nText, (Object[])parameters);
        } catch (InvalidPathException e) {
            System.err.println("!!! Unable to format '" + text + "': " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
