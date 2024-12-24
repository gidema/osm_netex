export class NetexRoute {
    public id: string;
    public lineId: string;
    public name: string;
    public network: string;
    public brandingRef: string;
    public directionType: string;
    public transportMode: string;
    public publicCode: string;
    //privateCode;
    public colour: string;
    public textColour: string;
    public mobilityImpairedAccess: boolean;
/*        constructor( 
        public id: string,
        public lineId: string,
        public name: string,
        public network: string,
        public brandingRef: string,
        public directionType: string,
        public transportMode: string,
        public publicCode: string,
        //privateCode;
        public colour: string,
        public textColour: string,
        public mobilityImpairedAccess: boolean) {};
*/

    constructor(route: NetexRoute) {
        this.id = route.id;
        this.lineId = route.lineId;
        this.name = route.name;
        this.network = route.network;
        this.brandingRef = route.brandingRef;
        this.directionType = route.directionType;
        this.brandingRef = route.brandingRef;
        this.transportMode = route.transportMode;
        this.publicCode = route.publicCode;
        this.colour = route.colour;
        this.textColour = route.textColour;
        this.mobilityImpairedAccess = route.mobilityImpairedAccess;
    };

    public get color(): string {
        return "#" + this.colour;
    }
}
