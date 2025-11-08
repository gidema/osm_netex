import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Observable } from 'rxjs';

import LineMatchService from '@lines/line-match.service';
import LineMatch from '@lines/line-match';
import RouteIssueComponent from '@issues/route-issue/route-issue.component';

@Component({
    selector: 'app-line-match-issues',
    imports: [ RouteIssueComponent ],
    templateUrl: './line-match-issues.component.html',
    styleUrl: './line-match-issues.component.css'
})
export default class LineMatchIssuesComponent implements OnInit {
    private route = inject(ActivatedRoute);
    private lineMatchService = inject(LineMatchService);
    match!: LineMatch;
    showMinor: Boolean = true;

    ngOnInit() {
        const snapshot = this.route.snapshot;
        const id = parseInt(snapshot.paramMap.get('id') ?? "");
        this.showMinor = JSON.parse(snapshot.queryParamMap.get('show_minor') ?? "true");
        this.lineMatchService.findIssues(id).subscribe(lm => {
            this.match = lm;
        })
   }
}
