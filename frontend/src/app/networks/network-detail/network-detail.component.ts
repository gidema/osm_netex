import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { LineService } from '../../lines/line.service';
import { Line } from '../../lines/line';

@Component({
    selector: 'app-network-detail',
    standalone: true,
    imports: [RouterModule],
    templateUrl: './network-detail.component.html',
    styleUrl: './network-detail.component.css'
})
export class NetworkDetailComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private lineService = inject(LineService);
    networkName: string | null = null;
    countryCode: string | null = null;
    lines: Line[] = [];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            this.networkName = route.get('networkName');
        });
        this.lineService.findByNetwork(this.networkName || "").subscribe((data: Line[]) => {
            this.lines = data;
            this.lines.sort((a, b) => this.compareLineNum(a.lineRef, b.lineRef));
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
