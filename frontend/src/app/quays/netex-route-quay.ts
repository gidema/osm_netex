export default class NetexRouteQuay {
    public pointOnRouteId!: string;
    public routeId!: string;
    public quayIndex!: number;
    public quayCode!: string;
    public town!: string;
    public quayName!: string;
    public stopSideCode!: string;
    public stopPlaceCode!: string;

    public get name(): string {
        return this.quayName + (this.stopSideCode ? " (" + this.stopSideCode + ")" : "");
    }
    
    public get uov(): string {
        var uov = this.town.toLowerCase() + "-" + this.quayName.toLowerCase();
        return uov.replaceAll(" ", "-").replaceAll("'", "").replaceAll("+", "");
    }

    public get _9292(): string {
        var town = this.town.toLowerCase().replaceAll(" ", "-").replaceAll("'", "");
        var quay = this.quayName.toLowerCase().replaceAll(" ", "-").replaceAll("'", "").replaceAll("+", "").replaceAll("/", "-");
        return town + "/bushalte-" + quay;
    }

}
