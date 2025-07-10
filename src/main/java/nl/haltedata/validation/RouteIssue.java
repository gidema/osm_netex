package nl.haltedata.validation;

public interface RouteIssue {
    public IssueSeverity getSeverity();

    public String getMessage(I18n i18n);
}
