export class NetexRouteVariantQuay {
    public sequenceId : Number;
    public quayIndex : Number;
    public lineNumber : String;
    public quayCode : String;
    public quayName : String;
    public town : String;
    public stopSideCode : String;
    public stopPlaceCode : String;
    public quayLocationType : String;

    constructor(quay: NetexRouteVariantQuay) {
        this.sequenceId = quay.sequenceId;
        this.quayIndex = quay.quayIndex;
        this.lineNumber = quay.lineNumber;
        this.quayCode = quay.quayCode;
        this.quayName = quay.quayName;
        this.town = quay.town;
        this.stopSideCode = quay.stopSideCode;
        this.stopPlaceCode = quay.stopPlaceCode;
        this.quayLocationType = quay.quayLocationType;
    }
    
    public get name(): string {
        return this.quayName + (this.stopSideCode ? " (" + this.stopSideCode + ")" : "");
    }
}
