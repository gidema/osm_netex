import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { OsmNetwork } from '../osm-network';
import { OsmNetworkService } from '../osm-network.service';

@Component({
  selector: 'app-network-list',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './osm_network-list.component.html'
})
export class NetworkListComponent implements OnInit {
  private networkService = inject(OsmNetworkService);
  networks!: OsmNetwork[];

  ngOnInit() {
    this.networkService.findAll().subscribe((data: OsmNetwork[]) => {
      this.networks = data;
    });
  }
}
