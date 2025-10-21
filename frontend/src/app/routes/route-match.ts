import RouteIssueData from '@routes/route-issue-data';
import LineMatch from '@lines/line-match';
import OsmRoute from '@routes/osm-route';
import NetexRouteVariant from '@routes/netex-route-variant';

export default class RouteMatch {
    id!: number;
    lineMatch?: LineMatch;
    osmRoute?: OsmRoute;
    netexVariant?: NetexRouteVariant;
    matchRate!: number;
    matching!: string;
    issues?: RouteIssueData[];
}
