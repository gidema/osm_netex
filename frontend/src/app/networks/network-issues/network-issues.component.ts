import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Observable } from 'rxjs';
import { NgPipesModule } from 'ngx-pipes';
import NetworkMatch from '@networks/network-match';
import NetworkMatchService from '@networks/network-match.service';
import RouteIssueComponent from '@issues/route-issue/route-issue.component';

@Component({
    selector: 'app-network-issues',
    imports: [RouterModule, NgPipesModule, RouteIssueComponent],
    templateUrl: './network-issues.component.html',
    styleUrl: './network-issues.component.css',
})
export default class NetworkIssuesComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private networkMatchService = inject(NetworkMatchService);
    networkMatch!: NetworkMatch;
    showMinor: Boolean = true;

    ngOnInit() {
        const snapshot = this.activatedRoute.snapshot;
        const administrativeZone = snapshot.paramMap.get('administrativeZone') ?? "";
        this.showMinor = JSON.parse(snapshot.queryParamMap.get('show_minor') ?? "true");
        this.networkMatchService.findIssues(administrativeZone).subscribe( n => {
            this.networkMatch = n;
            this.networkMatch.lineMatches.sort((a, b) => a.lineSort.localeCompare(b.lineSort));
        });
    }
}
