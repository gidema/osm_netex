import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { NetexNetwork } from '../netex-network';
import { NetexNetworkService } from '../netex-network.service';
import { NetexLineService } from '../../lines/netex-line-service.service';
import { NetexLine } from '../../lines/netex-line';

@Component({
    selector: 'netex-network-detail',
    imports: [RouterModule],
    templateUrl: './netex-network-detail.component.html'
})
export class NetexNetworkDetailComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private networkService = inject(NetexNetworkService);
    private lineService = inject(NetexLineService);
    netexNetwork: NetexNetwork | null = null;
    networkId: string | null = null;
    countryCode: string | null = null;
    lines: NetexLine[] = [];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            this.networkId = route.get('netexNetworkId');
            this.networkService.getById(this.networkId ?? "").subscribe(data => {
                this.netexNetwork = data;
        this.lineService.findByAdministrativeZone((this.netexNetwork?.administrativeZone) ?? "").subscribe((data: NetexLine[]) => {
            this.lines = data;
            this.lines.sort((a, b) => this.compareLineNum(a.publicCode, b.publicCode));
        });
            })
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
