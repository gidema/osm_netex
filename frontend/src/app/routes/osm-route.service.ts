import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import OsmRoute from '@routes/osm-route';

@Injectable({
    providedIn: 'root'
})
export default class OsmRouteService {
    private http = inject(HttpClient);
    private url: string = 'http://localhost:8080/osm/route';

    public findById(routeId: number): Observable<OsmRoute> {
        return this.http.get<OsmRoute>(this.url + "/" + routeId);
    }

    public findByLineId(lineId: number): Observable<OsmRoute[]> {
        const options = { params: new HttpParams().set('lineId', lineId) };
        return this.http.get<OsmRoute[]>(this.url, options);
    }
}
