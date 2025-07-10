package nl.haltedata.validation;

public class MinorRouteIssue implements RouteIssue {

    private final String message;
    private final Object[] parameters;

    public MinorRouteIssue(String message, Object... parameters) {
        this.message = message;
        this.parameters = parameters;
    }

    @Override
    public IssueSeverity getSeverity() {
        return IssueSeverity.Minor;
    }

    @Override
    public String getMessage(I18n i18n) {
        return i18n.tr(message, parameters);
    }
}