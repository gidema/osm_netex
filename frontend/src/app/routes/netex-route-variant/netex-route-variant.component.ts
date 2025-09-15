import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { NetexRouteVariantService } from '../netex-route-variant.service';
import { NetexRouteVariant } from '../netex-route-variant';
import { NetexRouteVariantQuay } from '../netex-route-variant-quay';
import { NetexRouteVariantQuayService } from '../netex-route-variant-quay.service';

@Component({
    selector: 'app-netex-quay-sequence',
    imports: [],
    templateUrl: './netex-route-variant.component.html',
    styleUrl: './netex-route-variant.component.css'
})
export class NetexRouteVariantComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private routeVariantService = inject(NetexRouteVariantService);
    private routeVariantQuayService = inject(NetexRouteVariantQuayService);
    variantId: number = 0;
    routeVariant!: NetexRouteVariant;
    quays: NetexRouteVariantQuay[] = [];

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((paramMap: ParamMap) => {
            this.variantId = parseInt(paramMap.get('variantId') ?? "0");
        });
        this.routeVariantService.findById(this.variantId).subscribe((data: NetexRouteVariant) => {
            this.routeVariant = data;
        });
        this.routeVariantQuayService.findByVariantId(this.variantId).subscribe((data: NetexRouteVariantQuay[]) => {
            this.quays = data;
        });
    }
}
