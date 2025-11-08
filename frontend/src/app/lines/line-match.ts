import RouteMatch from '@routes/route-match';
import OsmLine from '@lines/osm-line';
import NetexLine from '@lines/netex-line';
import IssueStats from '@issues/issue-stats';

export default class LineMatch {
    id!: number;
    administrativeZone!: string;
    countryCode!: string;
    network!: string;
    transportMode!: string;
    lineNumber!: string;
    lineSort!: string;
    osmLine?: OsmLine;
    netexLine?: NetexLine;
    productCategory!: string;
    routeMatches?: RouteMatch[];
    issueStats: IssueStats = new IssueStats();
}