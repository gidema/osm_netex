import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import OsmNetwork from '@networks/osm-network';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export default class OsmNetworkService {

  private networksUrl: string;

  constructor(private http: HttpClient) {
    this.networksUrl = 'http://localhost:8080/osm/network';
  }

  public getById(id: number): Observable<OsmNetwork> {
    return this.http.get<OsmNetwork>(this.networksUrl + `/${id}`);
  }

  public findAll(): Observable<OsmNetwork[]> {
    return this.http.get<OsmNetwork[]>(this.networksUrl);
  }
}