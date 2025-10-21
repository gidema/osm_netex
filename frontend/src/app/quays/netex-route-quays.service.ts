import { HttpClient, HttpUrlEncodingCodec } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import NetexRouteQuay from '@quays/netex-route-quay';

const codec = new HttpUrlEncodingCodec();

@Injectable({
  providedIn: 'root'
})
export default class NetexRouteQuaysService {
    private http = inject(HttpClient);
    private url: string = 'http://localhost:8080/netex/route';

  constructor() { }

    public findByRouteId(routeId: string): Observable<NetexRouteQuay[]> {
        return this.http.get<NetexRouteQuay[]>(this.url + "/" + codec.encodeValue(routeId) + "/quays");
    }
}
