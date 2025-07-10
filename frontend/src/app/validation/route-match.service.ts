import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { RouteMatch } from './route-match';

@Injectable({
  providedIn: 'root'
})
export class RouteValidationStatusService {
    private http = inject(HttpClient);
    private lineUrl: string = 'http://localhost:8080/osm/route/validate';

    public findByNetwork(network: String): Observable<RouteMatch[]> {
        return this.http.get<RouteMatch[]>(this.lineUrl + "/" + network);
    }

  constructor() { }
}
