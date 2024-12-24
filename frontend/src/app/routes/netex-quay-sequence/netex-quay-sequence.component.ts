import { Component, OnInit, inject } from '@angular/core';
import { NetexQuaySequenceService } from '../netex-quay-sequence.service';
import { NetexQuaySequence } from '../netex-quay-sequence';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { NetexQuayInSequence } from '../netex-quay-in-sequence';
import { NetexQuayInSequenceService } from '../netex-quay-in-sequence.service';

@Component({
  selector: 'app-netex-quay-sequence',
  standalone: true,
  imports: [],
  templateUrl: './netex-quay-sequence.component.html',
  styleUrl: './netex-quay-sequence.component.css'
})
export class NetexQuaySequenceComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private quaySequenceService = inject(NetexQuaySequenceService);
    private quayInSequenceService = inject(NetexQuayInSequenceService);
    sequenceId: number = 0;
    quaySequence!: NetexQuaySequence;
    quays: NetexQuayInSequence[] = [];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((paramMap: ParamMap) => {
            this.sequenceId = parseInt(paramMap.get('sequenceId') ?? "0");
        });
        this.quaySequenceService.findById(this.sequenceId).subscribe((data: NetexQuaySequence) => {
            this.quaySequence = data;
        });
        this.quayInSequenceService.findBySequenceId(this.sequenceId).subscribe((data: NetexQuayInSequence[]) => {
            this.quays = data;
        });
    }
}
