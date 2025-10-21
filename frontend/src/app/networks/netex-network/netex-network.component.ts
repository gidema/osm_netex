import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import NetexNetwork from '@networks/netex-network';
import NetexNetworkService from '@networks/netex-network.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'netex-network',
    imports: [RouterModule],
    templateUrl: './netex-network.component.html'
})
export default class NetexNetworkComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private networkService = inject(NetexNetworkService);
    network!: NetexNetwork;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            var networkId = route.get('netexNetworkId') ?? "";
            this.networkService.getById(networkId).subscribe(n => {
                this.network = n;
                this.network?.lines?.sort((a, b) => a.lineSort.localeCompare(b.lineSort));
            })
        });
    }
}
