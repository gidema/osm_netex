import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import NetworkMatch from '@networks/network-match';
import NetworkMatchService from '@networks/network-match.service';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { NgPipesModule } from 'ngx-pipes';

@Component({
    selector: 'app-network-issues',
    imports: [RouterModule, AsyncPipe, NgPipesModule],
    templateUrl: './network-issues.component.html',
    styleUrl: './network-issues.component.css'
})
export default class NetworkIssuesComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private networkMatchService = inject(NetworkMatchService);
    networkMatch$!: Observable<NetworkMatch>;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            const networkId = route.get('networkId') ?? "";
            this.networkMatch$ = this.networkMatchService.findById(networkId);
        });
    }
}
