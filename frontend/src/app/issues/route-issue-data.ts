import RouteMatch from '@routes/route-match';

export default class RouteIssueData {
    id!: number;
    routeMatch?: RouteMatch;
    sequence!: number;
    issueType!: string;
    parameters!: string[];
    lines!: string[];
    severity!: string;
}
