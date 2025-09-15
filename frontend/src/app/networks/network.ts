import { OsmNetwork } from './osm-network';
import { NetexNetwork } from './netex-network';

export class Network {
    id!: string;
    administrativeZone!: string;
    name!: string;
    shortName!: string;
    osmNetwork?: OsmNetwork;
    netexNetwork?: NetexNetwork;
}
