import { Routes } from '@angular/router';
import { NetworkListComponent } from './networks/network-list/network-list.component';
import { NetworkDetailComponent } from './networks/network-detail/network-detail.component';
import { OsmNetworkDetailComponent } from './networks/osm-network-detail/osm-network-detail.component';
import { NetexNetworkDetailComponent } from './networks/netex-network-detail/netex-network-detail.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { LineListComponent } from './lines/line-list/line-list.component';
import { RouteMatchComponent } from './routes/route-match/route-match.component';
import { NetexRouteComponent } from './routes/netex-route/netex-route.component';
import { LineDetailComponent } from './lines/line-detail/line-detail.component';
import { OsmLineDetailComponent } from './lines/osm-line-detail/osm-line-detail.component';
import { NetexLineDetailComponent } from './lines/netex-line-detail/netex-line-detail.component';
import { OsmRouteComponent } from './routes/osm-route/osm-route.component';
import { NetexRouteVariantComponent } from './routes/netex-route-variant/netex-route-variant.component';
// import { OsmRouteValidatorComponent } from './validation/osm-route-validator/osm-route-validator.component';

export const routes: Routes = [
  { path: '', redirectTo: 'list-networks', pathMatch: 'full' },
  { path: 'list-networks', component: NetworkListComponent },
  { path: 'routematches/:osmRouteId', component: RouteMatchComponent },
  { path: 'network/:networkName/route-match', component: RouteMatchComponent},
  { path: 'network/:networkId', component: NetworkDetailComponent},
//  { path: 'netex/route', component: NetexRouteComponent},
  { path: 'netex/network/:netexNetworkId', component: NetexNetworkDetailComponent},
  { path: 'netex/route/:routeId', component: NetexRouteComponent},
  { path: 'netex/route-variant/:variantId', component: NetexRouteVariantComponent},
  { path: 'line/:id', component: LineDetailComponent},
  { path: 'osm/network/:osmNetworkId', component: OsmNetworkDetailComponent},
  { path: 'osm/line/:osmLineId', component: OsmLineDetailComponent},
  { path: 'osm/route/:osmRouteId', component: OsmRouteComponent},
  { path: 'netex/line/:netexLineId', component: NetexLineDetailComponent},
  { path: 'network/:networkName/lines', component: LineListComponent},
  { path: '**', component: PageNotFoundComponent }
];