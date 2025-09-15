import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { OsmRoute } from '../../routes/osm-route';
import { OsmRouteService } from '../../routes/osm-route.service';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-osm-line-detail',
    imports: [RouterModule],
    templateUrl: './osm-line-detail.component.html',
    styleUrl: './osm-line-detail.component.css'
})
export class OsmLineDetailComponent   implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private routeService = inject(OsmRouteService);
    lineId: number = 0;
    routes: OsmRoute[] = [];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            this.lineId = parseInt(route.get('osmLineId') ?? "0");
        })
        this.routeService.findByLineId(this.lineId).subscribe((data: OsmRoute[]) => {
            this.routes = data;
//            this.routes.sort((a, b) => this.compareRoute(a, b))
        });
    }
}