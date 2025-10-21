import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import RouteMatchService from '@routes/route-match-service';
import RouteMatch from '@routes/route-match';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-route-match',
    imports: [RouterModule],
    templateUrl: './route-match.component.html',
    styleUrl: './route-match.component.css'
})
export default class RouteMatchComponent implements OnInit {
//    private activatedRoute = inject(ActivatedRoute);
    private routeMatchService = inject(RouteMatchService);
    match!: RouteMatch;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            const id = parseInt(route.get('id') ?? "");
            this.routeMatchService.findById(id).subscribe(rm => {
                this.match = rm;
            });
        });
    }
}
