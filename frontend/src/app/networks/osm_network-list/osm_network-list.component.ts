import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { OsmPtNetwork } from '../osm_network';
import { OsmPtNetworkService } from '../osm_network.service';

@Component({
  selector: 'app-network-list',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './osm_network-list.component.html'
})
export class NetworkListComponent implements OnInit {
  private networkService = inject(OsmPtNetworkService);
  networks!: OsmPtNetwork[];

  ngOnInit() {
    this.networkService.findAll().subscribe((data: OsmPtNetwork[]) => {
      this.networks = data;
    });
  }
}
