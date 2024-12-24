export class NetexQuayInSequence {
    public sequenceId : Number;
    public quayIndex : Number;
    public lineNumber : String;
    public quayCode : String;
    public quayName : String;
    public stopSideCode : String;
    public stopPlaceCode : String;
    public quayLocationType : String;

    constructor(quay: NetexQuayInSequence) {
        this.sequenceId = quay.sequenceId;
        this.quayIndex = quay.quayIndex;
        this.lineNumber = quay.lineNumber;
        this.quayCode = quay.quayCode;
        this.quayName = quay.quayName;
        this.stopSideCode = quay.stopSideCode;
        this.stopPlaceCode = quay.stopPlaceCode;
        this.quayLocationType = quay.quayLocationType;
    }
    
    public get name(): string {
        return this.quayName + (this.stopSideCode ? " (" + this.stopSideCode + ")" : "");
    }
}
