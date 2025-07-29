export class RouteMatch_ {
    public id: Number;
    public lineId: Number;
    public lineNumber: String;
    public lineSort: String;
    public osmRouteId: Number;
    public netexVariantId: Number;
    public network: String;
    public match_rate: Number;
    public matching: String;
    public netexLineNumber: Number;
    public netexName: null;
    public directionType: String;
    public osmName: String;
    public osmTransportMode: String;
    public osmLineNumber: Number;
    public from: String;
    public to: String;
    
    public constructor(match: RouteMatch) {
        this.id = match.id;
        this.lineId = match.lineId;
        this.network = match.network;
        this.osmRouteId = match.osmRouteId;
        this.netexVariantId = match.netexVariantId;
        this.match_rate = match.match_rate;
        this.matching = match.matching;
        this.netexLineNumber = match.netexLineNumber;
        this.netexName = match.netexName;
        this.directionType = match.directionType;
        this.network = match.network;
        this.osmName = match.osmName;
        this.osmTransportMode = match.osmTransportMode;
        this.osmLineNumber = match.osmLineNumber;
        this.from = match.from;
        this.to = match.to;
    }
}
