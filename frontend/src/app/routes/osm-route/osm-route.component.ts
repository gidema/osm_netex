import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import OsmRouteService from '@routes/osm-route.service';
import OsmRoute from '@routes/osm-route';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-osm-route',
    imports: [ ],
    templateUrl: './osm-route.component.html',
    styleUrl: './osm-route.component.css'
})
export default class OsmRouteComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private routeService = inject(OsmRouteService);
    osmRoute!: OsmRoute;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            const id = parseInt(route.get('osmRouteId') ?? "0");
            this.routeService.findById(id).subscribe(r => {
                this.osmRoute = r;
            })
        });
    }
}