export class NetexRouteQuay {
    constructor(
        public pointOnRouteId: String,
        public routeId: String,
        public quayIndex: Number,
        public quayCode: String,
        public place: String,
        public quayName: String,
        public stopSideCode: String,
        public stopplaceCode: String) {};
}
