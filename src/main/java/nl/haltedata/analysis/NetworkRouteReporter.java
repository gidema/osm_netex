package nl.haltedata.analysis;

import java.util.Locale;

public interface NetworkRouteReporter {
    public CharSequence getReport(String network, Locale locale);
}
