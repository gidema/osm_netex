import OsmLine from '@lines/osm-line';

export default class OsmNetwork {
    id: number = 0;
    name!: string;
    shortName!: string;
    operator!: string;
    startDate!: string;
    endDate!: string;
    wikidata!: string;
    note!: string;
    isConcessie!: boolean;
    lines?: OsmLine[];
}
