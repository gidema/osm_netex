import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import NetworkMatch from '@networks/network-match';
import NetworkMatchService from '@networks/network-match.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-network',
    imports: [RouterModule, NgPipesModule],
    templateUrl: './network.component.html',
    styleUrl: './network.component.css'
})
export default class NetworkComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private networkMatchService = inject(NetworkMatchService);
    networkMatch!: NetworkMatch;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            const networkId = route.get('networkId') ?? "";
            this.networkMatchService.findById(networkId).subscribe(n => {
                this.networkMatch = n;
                this.networkMatch.lineMatches.sort((a, b) => a.lineSort.localeCompare(b.lineSort));
            })
        });
    }
}
