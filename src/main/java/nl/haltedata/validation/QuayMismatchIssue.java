package nl.haltedata.validation;

public class QuayMismatchIssue implements RouteIssue {
    private int osmPosition;
    private String expectedQuay;
    private String foundQuay;

    public QuayMismatchIssue(QuayMatch mismatch, int osmPosition) {
        this.osmPosition = osmPosition;
        this.expectedQuay = mismatch.getOsmQuay().toString();
        this.foundQuay = mismatch.getNetexQuay().toString();
    }
    
    @Override
    public IssueSeverity getSeverity() {
        return IssueSeverity.Major;
    }

    @Override
    public String getMessage(I18n i18n) {
       return i18n.tr("Deviating quays at position {0}. Expected {1}, but found {2}.",
               osmPosition, expectedQuay, foundQuay);
    }
}
