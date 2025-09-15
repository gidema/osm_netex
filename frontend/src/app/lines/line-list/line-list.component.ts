import { Component, inject } from '@angular/core';
import { Line } from '../line';
import { LineService } from '../line.service';
import { ActivatedRoute, ParamMap } from '@angular/router';

@Component({
    selector: 'app-line-list',
    imports: [],
    templateUrl: './line-list.component.html',
    styleUrl: './line-list.component.css'
})
export class LineListComponent {
    private activatedRoute = inject(ActivatedRoute);
    private lineService = inject(LineService);
    private networkName : string | null = null;
    lines!: Line[];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route:ParamMap) => {
            this.networkName  = route.get('networkName');
        })
        if (this.networkName) {
            this.lineService.findByNetwork(this.networkName).subscribe((data: Line[]) => {
                this.lines = data;
            });
        }
    }
}
