import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { OsmPtNetwork } from '../network';
import { OsmPtNetworkService } from '../network.service';

@Component({
  selector: 'app-network-list',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './network-list.component.html',
  styleUrl: './network-list.component.css'
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
