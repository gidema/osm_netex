import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { NetexRouteQuay } from './netex-route-quay';

@Injectable({
  providedIn: 'root'
})
export class NetexRouteQuaysService {
    private http = inject(HttpClient);
    private url: string = 'http://localhost:8080/netex/route';

  constructor() { }
  

    public findByRouteId(routeId: string): Observable<NetexRouteQuay[]> {
        return this.http.get<NetexRouteQuay[]>(this.url + "/" + routeId + "/quays");
  }

}
