import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import NetworkMatch from '@networks/network-match';
import NetworkMatchService from '@networks/network-match.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-network-list',
    imports: [RouterModule],
    templateUrl: './network-matches.component.html'
})
export default class NetworkMatchesComponent implements OnInit {
  private networkMatchService = inject(NetworkMatchService);
  networkMatches!: NetworkMatch[];

  ngOnInit() {
    this.networkMatchService.findAll().subscribe(nms => {
        this.networkMatches = nms;
    });
  }
}
