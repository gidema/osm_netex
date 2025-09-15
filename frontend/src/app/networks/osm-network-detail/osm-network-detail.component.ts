import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { OsmNetwork } from '../osm-network';
import { OsmNetworkService } from '../osm-network.service';
import { OsmLineService } from '../../lines/osm-line-service.service';
import { OsmLine } from '../../lines/osm-line';

@Component({
    selector: 'osm-network-detail',
    imports: [RouterModule],
    templateUrl: './osm-network-detail.component.html'
})
export class OsmNetworkDetailComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private networkService = inject(OsmNetworkService);
    private lineService = inject(OsmLineService);
    osmNetwork: OsmNetwork | null = null;
    networkId: number | null = null;
    countryCode: string | null = null;
    lines: OsmLine[] = [];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            this.networkId = parseInt(route.get('osmNetworkId') || "");
            this.networkService.getById(this.networkId).subscribe(data => {
                this.osmNetwork = data;
            })
        });
        this.lineService.findByNetworkId(this.networkId ?? 0).subscribe((data: OsmLine[]) => {
            this.lines = data;
            this.lines.sort((a, b) => this.compareLineNum(a.lineNumber, b.lineNumber));
        });
    }
    
    /**
     * Comparator for public transport line nummers. Compare numeric values first
     */
    private compareLineNum(a: string, b:string) : number {
        if (!isNaN(parseInt(a)) && !isNaN(parseInt(b)) ) {
            return parseInt(a) - parseInt(b);
        }
        return a.localeCompare(b)
    }
}
