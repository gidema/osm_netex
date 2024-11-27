import { Routes } from '@angular/router';
import { NetworkListComponent } from './networks/network-list/network-list.component';
import { NetworkDetailComponent } from './networks/network-detail/network-detail.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { LineListComponent } from './lines/line-list/line-list.component';
import { RouteMatchComponent } from './routes/route-match/route-match.component';
import { NetexRouteComponent } from './routes/netex-route/netex-route.component';
import { LineDetailComponent } from './lines/line-detail/line-detail.component';
import { OsmLineDetailComponent } from './lines/osm-line-detail/osm-line-detail.component';
import { NetexLineDetailComponent } from './lines/netex-line-detail/netex-line-detail.component';
import { OsmRouteComponent } from './routes/osm-route/osm-route.component';

export const routes: Routes = [
  { path: '', redirectTo: 'list-networks', pathMatch: 'full' },
  { path: 'list-networks', component: NetworkListComponent },
  { path: 'routematches/:osmRouteId', component: RouteMatchComponent },
  { path: 'network/:networkName', component: NetworkDetailComponent},
//  { path: 'netex/route', component: NetexRouteComponent},
  { path: 'netex/route/:routeId', component: NetexRouteComponent},
  { path: 'line/:id', component: LineDetailComponent},
  { path: 'osm/line/:osmLineId', component: OsmLineDetailComponent},
  { path: 'osm/route/:osmRouteId', component: OsmRouteComponent},
  { path: 'netex/line/:netexLineId', component: NetexLineDetailComponent},
  { path: 'network/:networkName/lines', component: LineListComponent},
  { path: '**', component: PageNotFoundComponent }
];