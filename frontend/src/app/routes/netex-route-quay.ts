export class NetexRouteQuay {
    public pointOnRouteId: String;
    public routeId: String;
    public quayIndex: Number;
    public quayCode: String;
    public place: String;
    public quayName: String;
    public stopSideCode: String;
    public stopplaceCode: String;

    constructor(quay: NetexRouteQuay) {
        this.pointOnRouteId = quay.pointOnRouteId;
        this.routeId = quay.routeId;
        this.quayIndex = quay.quayIndex;
        this.quayCode = quay.quayCode;
        this.place = quay.place ?? "Unknown";
        this.quayName = quay.quayName ?? "Unknown";
        this.stopSideCode = quay.stopSideCode ?? "";
        this.stopplaceCode = quay.stopplaceCode;
    }

    public get name(): string {
        return this.quayName + (this.stopSideCode ? " (" + this.stopSideCode + ")" : "");
    }
    
    public get uov(): string {
        var uov = this.place.toLowerCase() + "-" + this.quayName.toLowerCase();
        return uov.replaceAll(" ", "-").replaceAll("'", "").replaceAll("+", "");
    }

    public get _9292(): string {
        var place = this.place.toLowerCase().replaceAll(" ", "-").replaceAll("'", "");
        var quay = this.quayName.toLowerCase().replaceAll(" ", "-").replaceAll("'", "").replaceAll("+", "").replaceAll("/", "-");
        return place + "/bushalte-" + quay;
    }

}
