import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Network } from '../network';
import { NetworkService } from '../network.service';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';

@Component({
    selector: 'app-network-list',
    imports: [RouterModule, AsyncPipe],
    templateUrl: './network-list.component.html'
})
export class NetworkListComponent implements OnInit {
  private networkService = inject(NetworkService);
  networks$!: Observable<Network[]>;

  ngOnInit() {
    this.networks$ = this.networkService.findAll();
  }
}
