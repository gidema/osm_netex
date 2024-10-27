export class OsmRouteQuay {
    constructor(
        public osmRouteId: Number,
        public rank: Number,
        public osmQuayNodeId: Number,
        public osmQuayWayId: Number,
        public quayCode: String,
        public quayNameN: String,
        public stopSideCodeN: String,
        public quayNameW: String,
        public stopSideCodeW: String,
        public place: String,
        public areaCode: String) {}
}

