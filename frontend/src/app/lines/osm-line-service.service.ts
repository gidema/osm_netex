import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {HttpParams} from "@angular/common/http";
import { OsmLine } from './osm-line';

@Injectable({
  providedIn: 'root'
})
export class OsmLineService {
        private http = inject(HttpClient);
        private lineUrl: string = 'http://localhost:8080/osm/line';

        public findByNetworkId(id: number): Observable<OsmLine[]> {
            const options = { params: new HttpParams().set('networkId', id) };
            return this.http.get<OsmLine[]>(this.lineUrl, options);
      }
    }
