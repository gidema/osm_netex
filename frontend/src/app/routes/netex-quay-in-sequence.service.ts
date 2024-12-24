import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { NetexQuayInSequence } from './netex-quay-in-sequence';

@Injectable({
  providedIn: 'root'
})
export class NetexQuayInSequenceService {

    private http = inject(HttpClient);
    private url: string = 'http://localhost:8080/netex/quay-sequence';

    constructor() { }


      public findBySequenceId(sequenceId: number): Observable<NetexQuayInSequence[]> {
          return this.http.get<NetexQuayInSequence[]>(this.url + "/" + sequenceId + "/quays");
    }
}
