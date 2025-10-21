import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {HttpParams} from "@angular/common/http";
import NetexLine from '@lines/netex-line';

@Injectable({
  providedIn: 'root'
})
export default class NetexLineService {
    private http = inject(HttpClient);
    private lineUrl: string = 'http://localhost:8080/netex/line';

    public findById(id: string): Observable<NetexLine> {
        const url = `${this.lineUrl}/${encodeURIComponent(id)}`;
        return this.http.get<NetexLine>(url);
    }

    public findByAdministrativeZone(administrativeZone: string): Observable<NetexLine[]> {
        const options = { params: new HttpParams().set('administrativeZone', administrativeZone) };
        return this.http.get<NetexLine[]>(this.lineUrl, options);
    }

    public findByNetworkId(networkId: string): Observable<NetexLine[]> {
        const options = { params: new HttpParams().set('networkId', networkId) };
        return this.http.get<NetexLine[]>(this.lineUrl, options);
    }
}
