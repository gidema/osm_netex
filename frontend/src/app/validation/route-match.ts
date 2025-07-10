export class RouteMatch {
    public id: Number;
    public lineId: Number;
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
    
    public constructor(status: RouteMatch) {
        this.id = status.id;
        this.lineId = status.lineId;
        this.network = status.network;
        this.osmRouteId = status.osmRouteId;
        this.netexVariantId = status.netexVariantId;
        this.match_rate = status.match_rate;
        this.matching = status.matching;
        this.netexLineNumber = status.netexLineNumber;
        this.netexName = status.netexName;
        this.directionType = status.directionType;
        this.network = status.network;
        this.osmName = status.osmName;
        this.osmTransportMode = status.osmTransportMode;
        this.osmLineNumber = status.osmLineNumber;
        this.from = status.from;
        this.to = status.to;
    }
}
