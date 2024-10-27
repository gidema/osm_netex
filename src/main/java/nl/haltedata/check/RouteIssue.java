package nl.haltedata.check;

public interface RouteIssue {
    public IssueSeverity getSeverity();

    public String getMessage();
}
