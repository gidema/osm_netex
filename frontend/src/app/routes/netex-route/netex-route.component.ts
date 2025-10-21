import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import NetexRoute from '@routes/netex-route';
import NetexRouteService from '@routes/netex-route.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-netex-route',
    imports: [],
    templateUrl: './netex-route.component.html',
    styleUrl: './netex-route.component.css'
})
export default class NetexRouteComponent implements OnInit {
    private activatedRoute = inject(ActivatedRoute);
    private routeService = inject(NetexRouteService);
    format: string = "html";
    netexRoute!: NetexRoute;

    ngOnInit() {
        this.activatedRoute.paramMap.subscribe((paramMap: ParamMap) => {
            const id = paramMap.get('routeId') || "";
            this.routeService.findById(id).subscribe(r => {
                this.netexRoute = r;
            })
        });
    }

}
