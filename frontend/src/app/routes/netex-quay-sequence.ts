export class NetexQuaySequence {
    constructor( 
        public id: Number,
        public lineNumber: String,
        public quayList: String[],
        public stopPlaceList: String[],
        public quayCount: Number,
        public startQuayCode: String,
        public endQuayCode: String,
        public startStopPlaceCode: String,
        public endStopPlaceCode: String,
        public lineRef: String,
        public routeRefs: String[],
        public directionType: String) {};
}
