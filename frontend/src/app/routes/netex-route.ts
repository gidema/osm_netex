import NetexRouteQuay from '@quays/netex-route-quay';

export default class NetexRoute {
    public id!: string;
    public lineId!: string;
    public name!: string;
    public network!: string;
    public brandingRef!: string;
    public directionType!: string;
    public transportMode!: string;
    public publicCode!: string;
    //privateCode;
    public colour!: string;
    public textColour!: string;
    public mobilityImpairedAccess!: boolean;
    public routeQuays?: NetexRouteQuay[];

    public get color(): string {
        return "#" + this.colour;
    }
}
