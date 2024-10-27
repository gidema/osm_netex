import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { NetexRouteService } from '../../routes/netex-route.service';
import { NetexRoute } from '../../routes/netex-route';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-netex-line-detail',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './netex-line-detail.component.html',
  styleUrl: './netex-line-detail.component.css'
})
export class NetexLineDetailComponent  implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private routeService = inject(NetexRouteService);
    lineId: string | null = null;
    routes: NetexRoute[] = [];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            this.lineId = route.get('netexLineId');
        })
        this.routeService.findByLineId(this.lineId || "").subscribe((data: NetexRoute[]) => {
            this.routes = data;
            this.routes.sort((a, b) => this.compareRoute(a, b))
        });
    }
    
    /**
     * Comparator for public transport route sorting. Sort direction, followed by id.
     */
    private compareRoute(a: NetexRoute, b:NetexRoute) : number {
        return a.directionType.localeCompare(b.directionType)
    }

}