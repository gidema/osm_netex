import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { OsmLine } from '../osm-line';
import { OsmLineService } from '../osm-line-service.service';
import { OsmRoute } from '../../routes/osm-route';
import { OsmRouteService } from '../../routes/osm-route.service';
import { RouterModule } from '@angular/router';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';

@Component({
    selector: 'app-osm-line-detail',
    imports: [RouterModule, AsyncPipe],
    templateUrl: './osm-line-detail.component.html',
    styleUrl: './osm-line-detail.component.css'
})
export class OsmLineDetailComponent   implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private lineService = inject(OsmLineService);
    private routeService = inject(OsmRouteService);
    osmLine$!: Observable<OsmLine>;
    routes$!: Observable<OsmRoute[]>;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            let lineId = parseInt(route.get('osmLineId') ?? "0");
            this.osmLine$ = this.lineService.getById(lineId);
            this.routes$ = this.routeService.findByLineId(lineId);
        });
    }
}