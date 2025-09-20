import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { OsmNetwork } from '../osm-network';
import { OsmNetworkService } from '../osm-network.service';
import { OsmLineService } from '../../lines/osm-line-service.service';
import { OsmLine } from '../../lines/osm-line';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { NgPipesModule } from 'ngx-pipes';

@Component({
    selector: 'osm-network-detail',
    imports: [RouterModule, AsyncPipe, NgPipesModule],
    templateUrl: './osm-network-detail.component.html'
})
export class OsmNetworkDetailComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private networkService = inject(OsmNetworkService);
    private lineService = inject(OsmLineService);
    osmNetwork$!: Observable<OsmNetwork>;
    lines$!: Observable<OsmLine[]>;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            let networkId = parseInt(route.get('osmNetworkId') || "");
            this.osmNetwork$ = this.networkService.getById(networkId);
            this.lines$ = this.lineService.findByNetworkId(networkId);
        });
    }
}
