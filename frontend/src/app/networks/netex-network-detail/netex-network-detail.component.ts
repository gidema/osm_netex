import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { NetexNetwork } from '../netex-network';
import { NetexNetworkService } from '../netex-network.service';
import { NetexLineService } from '../../lines/netex-line-service.service';
import { NetexLine } from '../../lines/netex-line';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { NgPipesModule } from 'ngx-pipes';

@Component({
    selector: 'netex-network-detail',
    imports: [RouterModule, AsyncPipe, NgPipesModule],
    templateUrl: './netex-network-detail.component.html'
})
export class NetexNetworkDetailComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private networkService = inject(NetexNetworkService);
    private lineService = inject(NetexLineService);
    netexNetwork$!: Observable<NetexNetwork>;
    lines$!: Observable<NetexLine[]>;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            let networkId = route.get('netexNetworkId') ?? "";
            this.netexNetwork$ = this.networkService.getById(networkId);
            this.lines$ = this.lineService.findByAdministrativeZone(networkId);
        });
    }
}
