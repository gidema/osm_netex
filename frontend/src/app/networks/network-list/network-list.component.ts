import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Network } from '../network';
import { NetworkService } from '../network.service';

@Component({
  selector: 'app-network-list',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './network-list.component.html'
})
export class NetworkListComponent implements OnInit {
  private networkService = inject(NetworkService);
  networks!: Network[];

  ngOnInit() {
    this.networkService.findAll().subscribe((data: Network[]) => {
      this.networks = data;
      this.networks.sort((a, b) => this.compareNetwork(a, b))
    });
  }
  
  /**
   * Comparator for public transport network sorting. Sort by name.
   */
  private compareNetwork(a: Network, b:Network) : number {
      return a.name.localeCompare(b.name)
  }

}
