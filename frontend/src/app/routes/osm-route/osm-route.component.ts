import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { OsmRouteService } from '../osm-route.service';
import { OsmRoute } from '../osm-route';
import { OsmRouteQuay } from '../osm-route-quay';
import { OsmRouteQuayService } from '../osm-route-quay.service';

@Component({
    selector: 'app-osm-route',
    imports: [],
    templateUrl: './osm-route.component.html',
    styleUrl: './osm-route.component.css'
})
export class OsmRouteComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private routeService = inject(OsmRouteService);
    private routeQuaysService = inject(OsmRouteQuayService);
    routeId: number = 0;
    osmRoute!: OsmRoute;
    routeQuays: OsmRouteQuay[] = [];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            this.routeId = parseInt(route.get('osmRouteId') ?? "0");
        });
        this.routeService.findById(this.routeId).subscribe((data: OsmRoute) => {
            this.osmRoute = data;
        });
        this.routeQuaysService.findByRouteId(this.routeId).subscribe((data: OsmRouteQuay[]) => {
            this.routeQuays = data;
    });
    }

}