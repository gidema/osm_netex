import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import OsmNetwork from '@networks/osm-network';
import OsmNetworkService from '@networks/osm-network.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'osm-network',
    imports: [RouterModule],
    templateUrl: './osm-network.component.html'
})
export default class OsmNetworkComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private networkService = inject(OsmNetworkService);
    network!: OsmNetwork;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            let networkId = parseInt(route.get('osmNetworkId') || "");
            this.networkService.getById(networkId).subscribe(n => {
                this.network = n;
                this.network.lines?.sort((a, b) => a.lineSort.localeCompare(b.lineSort));
            })
        });
    }
}
