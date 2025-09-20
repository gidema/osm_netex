import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RouteMatch } from '../routes/route-match';

@Injectable({
  providedIn: 'root'
})

export class RouteMatchService {
    private http = inject(HttpClient);
    private baseUrl: string = 'http://localhost:8080/route_match';

    public findById(id: number): Observable<RouteMatch> {
        return this.http.get<RouteMatch>(`${this.baseUrl}/${id}`);
    }

//    public findByNetwork(network: string | null): Observable<RouteMatch[]> {
//        const options = { params: new HttpParams().set('network', (network || '').toString()) };
//        return this.http.get<RouteMatch[]>(this.baseUrl + "/" + network + "/route_match");
//  }
}
