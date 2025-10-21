import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import NetexRouteVariantQuay from '@quays/netex-route-variant-quay';

@Injectable({
  providedIn: 'root'
})
export default class NetexRouteVariantQuayService {

    private http = inject(HttpClient);
    private url: string = 'http://localhost:8080/netex/route-variant';

    constructor() { }


      public findByVariantId(sequenceId: number): Observable<NetexRouteVariantQuay[]> {
          return this.http.get<NetexRouteVariantQuay[]>(this.url + "/" + sequenceId + "/quays");
    }
}
