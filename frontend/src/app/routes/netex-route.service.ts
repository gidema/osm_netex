import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { NetexRoute } from './netex-route';

@Injectable({
    providedIn: 'root'
})
export class NetexRouteService {
    private http = inject(HttpClient);
    private lineUrl: string = 'http://localhost:8080/netex/route';

    public findById(routeId: string): Observable<NetexRoute> {
        return this.http.get<NetexRoute>(this.lineUrl + "/" + routeId);
    }

    public findByLineId(lineId: string): Observable<NetexRoute[]> {
        const options = { params: new HttpParams().set('lineId', lineId) };
        return this.http.get<NetexRoute[]>(this.lineUrl, options);
    }
}
