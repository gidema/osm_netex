export default class NetexRouteVariantQuay {
    private sequenceId! : number;
    private quayIndex! : number;
    private lineNumber! : string;
    private quayCode! : string;
    private quayName! : string;
    private town! : string;
    private stopSideCode! : string;
    private stopPlaceCode! : string;
    private quayLocationType! : string;
    
    public get name(): string {
        return this.quayName + (this.stopSideCode ? " (" + this.stopSideCode + ")" : "");
    }
}
