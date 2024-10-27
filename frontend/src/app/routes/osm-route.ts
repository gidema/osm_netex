export class OsmRoute {
    constructor(
        public osmRouteId: number,
        public osmLineId: number,
        public transport_mode: string,
        public name: string,
        public operator: string,
        public routeRef: string,
        public network: string,
        public from: string,
        public to: string,
        public colour: string,
    ) { };
}
