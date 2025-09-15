import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap, Route } from '@angular/router';
import { Line } from '../line';
import { LineService } from '../line.service';
import { RouterModule } from '@angular/router';
import { TransportRouteService } from '../../routes/transport_route.service';
import { TransportRoute } from '../../routes/transport_route';

@Component({
    selector: 'app-line-detail',
    imports: [RouterModule],
    templateUrl: './line-detail.component.html',
    styleUrl: './line-detail.component.css'
})
export class LineDetailComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private lineService = inject(LineService);
    private routeService = inject(TransportRouteService);
    lineId!: number;
    line!: Line;
    osmLineId: number = 0;
    netexLineId: string | null = null;
    routes: TransportRoute[] = [];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            this.lineId = parseInt(route.get('id') ?? "0");
        })
        this.lineService.getById(this.lineId || 0).subscribe((data: Line) => {
            this.line = data;
            this.routeService.findByLineId(this.line.id).subscribe((data: TransportRoute[]) => {
               this.routes = data;
            });
        });
    }

}
