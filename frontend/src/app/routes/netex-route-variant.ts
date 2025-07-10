export class NetexRouteVariant {
    constructor( 
        public id: number,
        public name: string,
        public lineNumber: string,
        public quayList: string[],
        public stopPlaceList: string[],
        public quayCount: number,
        public startQuayCode: string,
        public endQuayCode: string,
        public startStopPlaceCode: string,
        public endStopPlaceCode: string,
        public lineRef: string,
        public routeRefs: string[],
        public directionType: string) {};
}
