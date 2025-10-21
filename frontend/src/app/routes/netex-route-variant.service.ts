import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject, Optional } from '@angular/core';
import { Observable } from 'rxjs';
import NetexRouteVariant from '@routes/netex-route-variant';

@Injectable({
  providedIn: 'root'
})
export default class NetexRouteVariantService {

    private http = inject(HttpClient);
    private routeUrl: string = 'http://localhost:8080/netex/route-variant';

    public findById(variantId: number): Observable<NetexRouteVariant> {
        var routeVariant : NetexRouteVariant | undefined = undefined;
        return this.http.get<NetexRouteVariant>(this.routeUrl + "/" + variantId);
    }

    public findByLineId(lineId: string): Observable<NetexRouteVariant[]> {
        const options = { params: new HttpParams().set('lineId', lineId) };
        return this.http.get<NetexRouteVariant[]>(this.routeUrl, options);
    }
}
