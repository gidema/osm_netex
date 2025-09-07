import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { OsmPtNetwork } from './osm_network';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OsmPtNetworkService {

  private networksUrl: string;

  constructor(private http: HttpClient) {
    this.networksUrl = 'http://localhost:8080/osm/network/list';
  }

  public getById(id: string): Observable<OsmPtNetwork> {
	const options = { params: new HttpParams().set('network', id) };
    return this.http.get<OsmPtNetwork>(this.networksUrl, options);
  }

  public findAll(): Observable<OsmPtNetwork[]> {
    return this.http.get<OsmPtNetwork[]>(this.networksUrl);
  }
//
//  public save(network: OsmPtNetwork) {
//    return this.http.post<OsmPtNetwork>(this.networksUrl, network);
//  }
}