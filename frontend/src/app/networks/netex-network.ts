import NetexLine from '@lines/netex-line';

export default class NetexNetwork {
    id: string = "";
    name!: string;
    shortName!: string;
    description!: string;
    fromDate!: Date;
    toDate!: Date;
    groupOfLinesType!: string;
    authorityRef!: string;
    fileSetId!: boolean;
    administrativeZone?: string;
    lines?: NetexLine[];
}
