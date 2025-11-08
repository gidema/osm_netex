import { Routes } from '@angular/router';
import NetworkMatchesComponent from '@networks/network-matches/network-matches.component';
import NetworkComponent from '@networks/network/network.component';
import NetworkIssuesComponent from '@networks/network-issues/network-issues.component';
import OsmNetworkComponent from '@networks/osm-network/osm-network.component';
import NetexNetworkComponent from '@networks/netex-network/netex-network.component';
import PageNotFoundComponent from '@page-not-found/page-not-found.component';
import RouteMatchComponent from '@routes/route-match/route-match.component';
import RouteMatchIssuesComponent from '@routes/route-match-issues/route-match-issues.component';
import NetexRouteComponent from '@routes/netex-route/netex-route.component';
import LineMatchComponent from '@lines/line-match/line-match.component';
import LineMatchIssuesComponent from '@lines/line-match-issues/line-match-issues.component';
import OsmLineComponent from '@lines/osm-line/osm-line.component';
import NetexLineComponent from '@lines/netex-line/netex-line.component';
import OsmRouteComponent from '@routes/osm-route/osm-route.component';
import NetexRouteVariantComponent from '@routes/netex-route-variant/netex-route-variant.component';

export const routes: Routes = [
  { path: '', redirectTo: 'network-matches', pathMatch: 'full' },
  { path: 'network-matches', component: NetworkMatchesComponent },
  { path: 'network/:networkId', component: NetworkComponent},
  { path: 'network/zone/:administrativeZone/issues', component: NetworkIssuesComponent},
  { path: 'netex/network/:netexNetworkId', component: NetexNetworkComponent},
  { path: 'line-match/:id', component: LineMatchComponent},
  { path: 'line-match/:id/issues', component: LineMatchIssuesComponent},
  { path: 'osm/network/:osmNetworkId', component: OsmNetworkComponent},
  { path: 'osm/line/:osmLineId', component: OsmLineComponent},
  { path: 'netex/line/:netexLineId', component: NetexLineComponent},
  { path: 'netex/route/:routeId', component: NetexRouteComponent},
  { path: 'netex/route-variant/:variantId', component: NetexRouteVariantComponent},
  { path: 'route-match/:id', component: RouteMatchComponent },
  { path: 'route-match/:id/issues', component: RouteMatchIssuesComponent },
  { path: 'osm/route/:osmRouteId', component: OsmRouteComponent},
  { path: '**', component: PageNotFoundComponent }
];