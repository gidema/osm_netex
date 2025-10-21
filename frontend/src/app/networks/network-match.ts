import OsmNetwork from '@networks/osm-network';
import NetexNetwork from '@networks/netex-network';
import LineMatch from '@lines/line-match';

export default class NetworkMatch {
    id!: string;
    administrativeZone!: string;
    name!: string;
    shortName!: string;
    osmNetwork?: OsmNetwork;
    netexNetwork?: NetexNetwork;
    lineMatches!: LineMatch[];
}
