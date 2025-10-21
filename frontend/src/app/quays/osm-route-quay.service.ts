import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import OsmRouteQuay from '@quays/osm-route-quay';

@Injectable({
  providedIn: 'root'
})
export default class OsmRouteQuayService {
      private http = inject(HttpClient);
      private url: string = 'http://localhost:8080/osm/route';

    constructor() { }


    public findByRouteId(routeId: number): Observable<OsmRouteQuay[]> {
          return this.http.get<OsmRouteQuay[]>(this.url + "/" + routeId + "/quays");
    }
}
