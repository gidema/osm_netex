import ChbQuay from '@quays/chb-quay'
import NetexRouteQuay from '@quays/netex-route-quay'

export default class NetexRouteVariant {
    public id!: number;
    public name!: string;
    public lineNumber!: string;
    public lineRef!: string;
    public directionType!: string;
    public quays: NetexRouteQuay[] = [];
        
    public get fromQuay(): NetexRouteQuay {
        return this.quays[0];
    }

    public get toQuay(): NetexRouteQuay {
        return this.quays[this.quays.length - 1];
    }

}
