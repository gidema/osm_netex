import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {HttpParams} from "@angular/common/http";
import { TransportRoute } from './transport_route';

@Injectable({
  providedIn: 'root'
})
export class TransportRouteService {
      private http = inject(HttpClient);
      private lineUrl: string = 'http://localhost:8080/route';

      public getById(id: number): Observable<TransportRoute> {
          return this.http.get<TransportRoute>(this.lineUrl + "/" + id);
      }

      public findByLineId(lineId: number): Observable<TransportRoute[]> {
          const options = { params: new HttpParams().set('lineId', lineId) };

          return this.http.get<TransportRoute[]>(this.lineUrl, options);
    }
}
