import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { RouteMatchService } from '../route_match_service';
import { RouteMatch } from '../route_match';

@Component({
  selector: 'app-route-match',
  standalone: true,
  imports: [],
  templateUrl: './route-match.component.html',
  styleUrl: './route-match.component.css'
})
export class RouteMatchComponent implements OnInit {
//    private activatedRoute = inject(ActivatedRoute);
    private routeMatchService = inject(RouteMatchService);
    private network: string | null = '';
    routeMatches: RouteMatch[] = [];
    selection: RouteMatch[] = [];

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
//       this.network = this.activatedRoute.snapshot.paramMap.get('networkName');
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            this.network = route.get('networkName');
         });
//       this.activatedRoute.paramMap.subscribe((route:ParamMap) => {
//           this.network  = route.get('networkName');
//       })
        this.routeMatchService.findByNetwork(this.network).subscribe((data: RouteMatch[]) => {
            this.routeMatches = data;
            this.selection = data.filter((match) => !match.matching.startsWith("Exact"))
                .sort((a, b) => {
                    if (a.lineSort < b.lineSort) return -1;
                    if (a.lineSort > b.lineSort) return 1;
           // names must be equal
                return 0;})
        });
   }
}
