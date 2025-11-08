import LineMatch from '@lines/line-match';
import OsmRoute from '@routes/osm-route';
import NetexRouteVariant from '@routes/netex-route-variant';
import RouteIssueData from '@issues/route-issue-data';
import IssueStats from '@issues/issue-stats';

export default class RouteMatch {
    id!: number;
    lineMatch?: LineMatch;
    osmRoute?: OsmRoute;
    netexVariant?: NetexRouteVariant;
    matchRate!: number;
    matching!: string;
    issues?: RouteIssueData[];
    issueStats?: IssueStats;
}
