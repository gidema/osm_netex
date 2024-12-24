import { Component, OnInit, inject } from '@angular/core';
import { NetexRouteService } from '../netex-route.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { NetexRoute } from '../netex-route';
import { NetexRouteQuay } from '../netex-route-quay';
import { NetexRouteQuaysService } from '../netex-route-quays.service';

@Component({
  selector: 'app-netex-route',
  standalone: true,
  imports: [],
  templateUrl: './netex-route.component.html',
  styleUrl: './netex-route.component.css'
})
export class NetexRouteComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private routeService = inject(NetexRouteService);
    private routeQuaysService = inject(NetexRouteQuaysService);
    routeId: string | null = null;
    format: string = "html";
    netexRoute!: NetexRoute;
    routeQuays: NetexRouteQuay[] = [];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((paramMap: ParamMap) => {
            this.routeId = paramMap.get('routeId');
        });
        this.activatedRoute.queryParamMap.subscribe((paramMap: ParamMap) => {
            this.format = paramMap.get('format') ?? "html";
        });
        this.routeService.findById(this.routeId || "").subscribe((data: NetexRoute) => {
            this.netexRoute = new NetexRoute(data);
        });
        this.routeQuaysService.findByRouteId(this.routeId || "").subscribe((data: NetexRouteQuay[]) => {
            this.routeQuays = data.flatMap(quay => new NetexRouteQuay(quay));
    });
    }

}
