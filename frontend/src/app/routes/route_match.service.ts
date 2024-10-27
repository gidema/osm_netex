import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {HttpParams} from "@angular/common/http";
import { RouteMatch } from '../routes/route_match';

@Injectable({
  providedIn: 'root'
})
export class RouteMatchService {
    private http = inject(HttpClient);
    private lineUrl: string = 'http://localhost:8080/routematches';

    public findByOsmId(osmRouteId: number): Observable<RouteMatch[]> {

        return this.http.get<RouteMatch[]>(this.lineUrl + "/" + osmRouteId);
  }
}
