import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { RouteValidationStatus } from './route-validation-status';

@Injectable({
  providedIn: 'root'
})
export class RouteValidationStatusService {
    private http = inject(HttpClient);
    private lineUrl: string = 'http://localhost:8080/osm/route/validate';

    public findByNetwork(network: String): Observable<RouteValidationStatus[]> {
        return this.http.get<RouteValidationStatus[]>(this.lineUrl + "/" + network);
    }

  constructor() { }
}
