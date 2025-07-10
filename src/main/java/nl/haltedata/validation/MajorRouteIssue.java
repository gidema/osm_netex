package nl.haltedata.validation;

public class MajorRouteIssue implements RouteIssue {

    private final String message;
    private final Object[] parameters;

    public MajorRouteIssue(String message, Object... parameters) {
        this.message = message;
        this.parameters = parameters;
    }

    @Override
    public IssueSeverity getSeverity() {
        return IssueSeverity.Major;
    }

    @Override
    public String getMessage(I18n i18n) {
        return i18n.tr(message, parameters);
    }
}
