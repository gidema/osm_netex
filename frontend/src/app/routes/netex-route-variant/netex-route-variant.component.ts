import { Component, OnInit, inject, Pipe, PipeTransform } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import NetexRouteVariantService from '@routes/netex-route-variant.service';
import NetexRouteVariant from '@routes/netex-route-variant';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';

@Component({
    selector: 'app-netex-quay-sequence',
//    imports: [ AsyncPipe ],
    templateUrl: './netex-route-variant.component.html',
    styleUrl: './netex-route-variant.component.css'
})
export default class NetexRouteVariantComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private routeVariantService = inject(NetexRouteVariantService);
    routeVariant!: NetexRouteVariant;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((paramMap: ParamMap) => {
            const id = parseInt(paramMap.get('variantId') ?? "0");
            this.routeVariantService.findById(id).subscribe(rv => {
                this.routeVariant = rv;
                this.routeVariant?.quays.sort((a, b) => a.quayIndex - b.quayIndex);
            });
        });
    }
}
