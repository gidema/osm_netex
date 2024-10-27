import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { OsmRoute } from './osm-route';

@Injectable({
    providedIn: 'root'
})
export class OsmRouteService {
    private http = inject(HttpClient);
    private lineUrl: string = 'http://localhost:8080/osm/route';

    public findById(routeId: string): Observable<OsmRoute> {
        return this.http.get<OsmRoute>(this.lineUrl + "/" + routeId);
    }

    public findByLineId(lineId: number): Observable<OsmRoute[]> {
        const options = { params: new HttpParams().set('lineId', lineId) };
        return this.http.get<OsmRoute[]>(this.lineUrl, options);
    }
}
