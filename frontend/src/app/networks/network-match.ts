import OsmNetwork from '@networks/osm-network';
import NetexNetwork from '@networks/netex-network';
import LineMatch from '@lines/line-match';
import IssueStats from '@issues/issue-stats';

export default class NetworkMatch {
    id!: string;
    administrativeZone!: string;
    name!: string;
    shortName!: string;
    osmNetwork?: OsmNetwork;
    netexNetwork?: NetexNetwork;
    lineMatches!: LineMatch[];
    issueStats?: IssueStats;
}
