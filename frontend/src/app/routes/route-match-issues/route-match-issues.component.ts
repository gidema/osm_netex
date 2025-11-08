import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Observable } from 'rxjs';

import RouteMatchService from '@routes/route-match-service';
import RouteMatch from '@routes/route-match';
import RouteIssueComponent from '@issues/route-issue/route-issue.component';

@Component({
    selector: 'app-route-match-issues',
    imports: [ RouteIssueComponent ],
    templateUrl: './route-match-issues.component.html',
    styleUrl: './route-match-issues.component.css'
})
export default class RouteMatchIssuesComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private routeMatchService = inject(RouteMatchService);
    match!: RouteMatch;

    ngOnInit() {
//       this.network = this.activatedRoute.snapshot.paramMap.get('networkName');
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            const id = parseInt(route.get('id') ?? "");
            this.routeMatchService.findById(id).subscribe(rm => {
                this.match = rm;
            })
        });
   }
}
