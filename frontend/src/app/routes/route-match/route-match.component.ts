import { Component, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { RouterModule } from '@angular/router';
import { RouteMatchService } from '../route_match.service';
import { RouteMatch } from '../route_match';

@Component({
  selector: 'app-route-match',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './route-match.component.html',
  styleUrl: './route-match.component.css'
})
export class RouteMatchComponent {
    private activatedRoute = inject(ActivatedRoute);
    private service = inject(RouteMatchService);
    private osmRouteId : number = -1;
    matches!: RouteMatch[];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route:ParamMap) => {
            this.osmRouteId  = parseInt(route.get('osmRouteId') ?? '-1');
        })
        if (this.osmRouteId > 0) {
            this.service.findByOsmId(this.osmRouteId).subscribe((data: RouteMatch[]) => {
                this.matches = data;
            });
        }
    }

}
