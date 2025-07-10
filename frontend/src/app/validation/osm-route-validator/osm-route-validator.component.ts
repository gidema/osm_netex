import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { RouteValidationStatusService } from '../route-validation-status.service';
import { RouteValidationStatus } from '../route-validation-status';

@Component({
  selector: 'app-osm-route-validator',
  standalone: true,
  imports: [],
  templateUrl: './osm-route-validator.component.html',
  styleUrl: './osm-route-validator.component.css'
})
export class OsmRouteValidatorComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private routeService = inject(RouteValidationStatusService);
    network: String = "";
    statuses: RouteValidationStatus[] = [];
    selected: RouteValidationStatus[] = [];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((param: ParamMap) => {
            this.network = param.get('network') ?? "";
        });
        this.routeService.findByNetwork(this.network).subscribe((data: RouteValidationStatus[]) => {
            this.statuses = data;
            this.sortByLineNumberAsc();
            this.selected = this.statuses.filter((st) => OsmRouteValidatorComponent.filterSelection(st));
        });
    }
    
    static filterSelection(status: RouteValidationStatus): boolean {
        return (OsmRouteValidatorComponent.filterBus(status) && !OsmRouteValidatorComponent.filterIssues(status));
    }
    
    static filterBus(status: RouteValidationStatus): boolean {
        return status.transportMode == "bus";
    }

    static filterIssues(status: RouteValidationStatus): boolean {
        return status.matching == "1 Quays match" || status.matching == "2 Stopplaces match";
    }

    public sortByLineNumberDesc(): void {
        this.selected = this.selected.sort((a, b) => a.lineNumber - b.lineNumber);
      }

    public sortByLineNumberAsc() {
        this.selected = this.selected.sort((a, b) => b.lineNumber - a.lineNumber);
    }


}