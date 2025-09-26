package nl.haltedata.analysis;

import java.util.Locale;

import nl.haltedata.analysis.dto.NetworkMatch;

public interface NetworkRouteReporter {
    public CharSequence getReport(NetworkMatch networkMatch, Locale locale);
}
