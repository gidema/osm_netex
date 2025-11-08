import { Component, Input } from '@angular/core';
import RouteIssueData from '@issues/route-issue-data';

@Component({
  selector: 'app-route-issue',
  imports: [],
  templateUrl: './route-issue.component.html',
  styleUrl: './route-issue.component.css'
})
export default class RouteIssueComponent {
    @Input() issue!: RouteIssueData;
    @Input() showMinor!: Boolean;
}
