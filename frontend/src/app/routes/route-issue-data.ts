import { RouteMatch } from './route-match';

export class RouteIssueData {
    id!: number;
    routeMatch?: RouteMatch;
    sequence!: number;
    message!: string;
    parameters!: string[];
}
