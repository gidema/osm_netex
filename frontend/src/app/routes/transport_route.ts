export class TransportRoute {
    constructor(
        public id: number,
        public lineId: number,
        public osmRouteId: number,
        public netexRouteId: number,
        public matching: string,
        public netexLineNumber: string,
        public netexName: string,
        public directionType: string,
        public osmName: string,
        public osmTransportMode: string,
        public osmLineNumber: string,
        public from: string,
        public to: string
    ) { };
}
