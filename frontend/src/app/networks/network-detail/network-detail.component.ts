import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Network } from '../network';
import { NetworkService } from '../network.service';
import { LineService } from '../../lines/line.service';
import { Line } from '../../lines/line';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { NgPipesModule } from 'ngx-pipes';

@Component({
    selector: 'app-network-detail',
    imports: [RouterModule, AsyncPipe, NgPipesModule],
    templateUrl: './network-detail.component.html',
    styleUrl: './network-detail.component.css'
})
export class NetworkDetailComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private networkService = inject(NetworkService);
    private lineService = inject(LineService);
    network$!: Observable<Network>;
    lines$!: Observable<Line[]>;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            const networkId = route.get('networkId') ?? "";
            this.network$ = this.networkService.getNetwork(networkId);
            this.network$.subscribe(network => {
                this.lines$ = this.lineService.findByAdministrativeZone(network.administrativeZone)
            });
        });
    }
}
