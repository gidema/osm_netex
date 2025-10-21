export default class OsmRouteQuay {
    constructor( 
        public osmRouteId: Number,
        public quayIndex: Number,
        public quayCode: String,
        public stopPlaceCode: String,
        public quayName: String,
        public stopSideCode: String,
        public stopPlace: String) {};
}

