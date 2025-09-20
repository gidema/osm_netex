import { RouteIssueData } from './route-issue-data';

export class RouteMatch {
    id!: number;
    lineId!: number;
    osmRouteId!: number;
    lineNumber!: String;
    lineSort!: String;
    variantId!: number;
    matchRate!: number;
    matching!: string;
    network!: string;
    netexLineNumber!: string;
    netexName!: string;
    directionType!: string;
    osmName!: string;
    osmTransportMode!: string;
    osmLineNumber!: string;
    from!: string;
    to!: string;
    issues?: RouteIssueData[];
}
