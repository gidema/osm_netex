package nl.haltedata.analysis;

import java.util.Locale;

import nl.haltedata.analysis.dto.NetworkMatchDto;

public interface NetworkRouteReporter {
    @SuppressWarnings("exports")
    public CharSequence getReport(NetworkMatchDto networkMatch, Locale locale);
}
