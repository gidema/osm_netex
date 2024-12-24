import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { NetexQuaySequence } from './netex-quay-sequence';

@Injectable({
  providedIn: 'root'
})
export class NetexQuaySequenceService {

    private http = inject(HttpClient);
    private routeUrl: string = 'http://localhost:8080/netex/quay-sequence';

    public findById(routeId: number): Observable<NetexQuaySequence> {
        return this.http.get<NetexQuaySequence>(this.routeUrl + "/" + routeId);
    }

//    public findByLineId(lineId: string): Observable<NetexQuaySequence[]> {
//        const options = { params: new HttpParams().set('lineId', lineId) };
//        return this.http.get<NetexQuaySequence[]>(this.routeUrl, options);
//    }
}
