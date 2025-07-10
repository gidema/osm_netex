import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { NetexRouteService } from '../../routes/netex-route.service';
import { NetexRoute } from '../../routes/netex-route';
import { RouterModule } from '@angular/router';
import { NetexRouteVariantService } from '../../routes/netex-route-variant.service';
import { NetexLineService } from '../netex-line-service.service';
import { NetexRouteVariant } from '../../routes/netex-route-variant';
import { NetexLine } from '../netex-line';

@Component({
  selector: 'app-netex-line-detail',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './netex-line-detail.component.html',
  styleUrl: './netex-line-detail.component.css'
})
export class NetexLineDetailComponent  implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private lineService = inject(NetexLineService);
    private routeService = inject(NetexRouteService);
    private routeVariantService = inject(NetexRouteVariantService);
    lineId: string | null = null;
    line!: NetexLine;
    routes: NetexRoute[] = [];
    routeVariants: NetexRouteVariant[] = [];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            this.lineId = route.get('netexLineId');
        })
        this.lineService.getById(this.lineId || "").subscribe((data: NetexLine) => {
            this.line = data;
        });
        this.routeService.findByLineId(this.lineId || "").subscribe((data: NetexRoute[]) => {
            this.routes = data;
            this.routes.sort((a, b) => this.compareRoute(a, b))
        });
        this.routeVariantService.findByLineId(this.lineId || "").subscribe((data: NetexRouteVariant[]) => {
            this.routeVariants = data;
            this.routeVariants.sort((a, b) => this.compareRouteVariant(a, b))
        });
    }
    
    /**
     * Comparator for public transport route sorting. Sort direction, followed by id.
     */
    private compareRoute(a: NetexRoute, b:NetexRoute) : number {
        return a.directionType.localeCompare(b.directionType)
    }

    /**
     * Comparator for public transport route sorting. Sort direction, followed by id.
     */
    private compareRouteVariant(a: NetexRouteVariant, b:NetexRouteVariant) : number {
        return a.directionType.localeCompare(b.directionType)
    }

}