import OsmRouteQuay from '@quays/osm-route-quay';

export default class OsmRoute {
    constructor( 
        public osmRouteId: Number,
        public osmLineId: Number,
        public name: String,
        public transportMode: String,
        public routeRef: String,
        public operator: String,
        public network: String,
        public from: String,
        public to: String,
        public colour: String,
        public quays?: OsmRouteQuay[]) {};
}
