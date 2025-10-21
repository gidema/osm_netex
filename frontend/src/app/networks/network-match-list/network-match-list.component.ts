import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import NetworkMatch from '@networks/network-match';
import NetworkMatchService from '@networks/network-match.service';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';

@Component({
    selector: 'app-network-list',
    imports: [RouterModule, AsyncPipe],
    templateUrl: './network-match-list.component.html'
})
export default class NetworkMatchListComponent implements OnInit {
  private networkMatchService = inject(NetworkMatchService);
  networkMatches$!: Observable<NetworkMatch[]>;

  ngOnInit() {
    this.networkMatches$ = this.networkMatchService.findAll();
  }
}
