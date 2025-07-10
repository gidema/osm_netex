export class RouteValidationStatus {
    public network: String;
    public osmRouteId: Number;
    public line: string;
    public lineNumber: number;
    public name: String;
    public transportMode: String;
    public matching: String;
    public issue: String;
    
    public constructor(status: RouteValidationStatus) {
        this.network = status.network;
        this.osmRouteId = status.osmRouteId;
        this.line = status.line;
        this.lineNumber = status.lineNumber;
        this.name = status.name;
        this.transportMode = status.transportMode;
        this.matching = status.matching;
        this.issue = status.issue;
    }
}
