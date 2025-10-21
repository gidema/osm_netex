import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {HttpParams} from "@angular/common/http";
import LineMatch from '@lines/line-match';

@Injectable({
  providedIn: 'root'
})
export default class LineMatchService {
    private http = inject(HttpClient);
    private lineUrl: string = 'http://localhost:8080/line-match';

    public getById(id: number): Observable<LineMatch> {
        return this.http.get<LineMatch>(this.lineUrl + "/" + id);
    }

    public findByAdministrativeZone(administrativeZone: string): Observable<LineMatch[]> {
        const options = { params: new HttpParams().set('administrativeZone', administrativeZone) };
        return this.http.get<LineMatch[]>(this.lineUrl, options);
    }

    public findByNetwork(networkName: string): Observable<LineMatch[]> {
        const options = { params: new HttpParams().set('network', networkName) };
        return this.http.get<LineMatch[]>(this.lineUrl, options);
  }
}
