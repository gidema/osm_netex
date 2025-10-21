import OsmRoute from '@routes/osm-route';

export default class OsmLine {
    id!: number;
    osmTransportMode!: string;
    netexTransportMode!: string;
    name!: string
    lineSort!: string;
    lineNumber!: string;
    network!: string;
    colour!: string;
    routes?: OsmRoute[];
}
