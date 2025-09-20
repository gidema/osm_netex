import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { RouteMatchService } from '../route-match-service';
import { RouteMatch } from '../route-match';
import { RouteIssueData } from '../route-issue-data';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';

@Component({
    selector: 'app-route-match',
    imports: [AsyncPipe],
    templateUrl: './route-match.component.html',
    styleUrl: './route-match.component.css'
})
export class RouteMatchComponent implements OnInit {
//    private activatedRoute = inject(ActivatedRoute);
    private routeMatchService = inject(RouteMatchService);
    routeMatch$?: Observable<RouteMatch>;
//    selection: RouteMatch[] = [];

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
//       this.network = this.activatedRoute.snapshot.paramMap.get('networkName');
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            const id = parseInt(route.get('id') ?? "");
            this.routeMatch$ = this.routeMatchService.findById(id);
        });
   }
}
