import { Component, Input, inject} from '@angular/core';
import { OsmPtNetwork } from '../osm_network';
import { OsmPtNetworkService } from '../osm_network.service';

@Component({
  selector: 'app-network',
  standalone: true,
  imports: [],
  templateUrl: './network.component.html',
  styleUrl: './network.component.css'
})
export class NetworkComponent {
  private networkService = inject(OsmPtNetworkService);
  network!: OsmPtNetwork;
  networkId!: string;

  @Input()set id(networkId: string) {
    this.networkService.getById(networkId).subscribe((data: OsmPtNetwork) => {
     this.network = data;
     });
  };

}
