import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap, Route } from '@angular/router';
import LineMatch from '@lines/line-match';
import LineMatchService from '@lines/line-match.service';
import { RouterModule } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-line-match',
    imports: [RouterModule],
    templateUrl: './line-match.component.html',
    styleUrl: './line-match.component.css'
})
export default class LineMatchComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private lineMatchService = inject(LineMatchService);
    lineMatch!: LineMatch;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((route: ParamMap) => {
            var lineId = parseInt(route.get('id') ?? "0");
            this.lineMatchService.getById(lineId).subscribe(lm => {
                this.lineMatch = lm;
                this.lineMatch.routeMatches?.sort((a, b) => a.netexVariant?.directionType.localeCompare(b.netexVariant?.directionType || "") || 0);
            })
        })
    }
}
