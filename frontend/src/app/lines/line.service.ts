import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {HttpParams} from "@angular/common/http";
import { Line } from '../lines/line';

@Injectable({
  providedIn: 'root'
})
export class LineService {
    private http = inject(HttpClient);
    private lineUrl: string = 'http://localhost:8080/line';

    public getById(id: number): Observable<Line> {
        return this.http.get<Line>(this.lineUrl + "/" + id);
    }

    public findByNetwork(networkName: string): Observable<Line[]> {
        const options = { params: new HttpParams().set('network', networkName) };

        return this.http.get<Line[]>(this.lineUrl, options);
  }
}
