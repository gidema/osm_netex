import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import OsmLine from '@lines/osm-line';
import OsmLineService from '@lines/osm-line-service.service';
import { RouterModule } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-osm-line',
    imports: [RouterModule],
    templateUrl: './osm-line.component.html',
    styleUrl: './osm-line.component.css'
})
export default class OsmLineComponent   implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private lineService = inject(OsmLineService);
    line!: OsmLine;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            const id = parseInt(route.get('osmLineId') ?? "0");
            this.lineService.getById(id).subscribe(l => {
                this.line = l;
            })
        });
    }
}