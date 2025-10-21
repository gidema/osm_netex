import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { RouterModule } from '@angular/router';
import NetexLine from '@lines/netex-line';
import NetexLineService from '@lines/netex-line-service.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-netex-line',
    imports: [RouterModule],
    templateUrl: './netex-line.component.html',
    styleUrl: './netex-line.component.css'
})
export default class NetexLineComponent  implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private lineService = inject(NetexLineService);
    line!: NetexLine;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            var lineId = route.get('netexLineId');
            this.lineService.findById(lineId || "").subscribe(ln => {
                this.line = ln;
                this.line.routeVariants?.sort((a, b) => a.directionType.localeCompare(b.directionType));
            })
        })
    }
}