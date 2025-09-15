import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NetexNetwork } from './netex-network';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NetexNetworkService {

  private networksUrl: string;

  constructor(private http: HttpClient) {
    this.networksUrl = 'http://localhost:8080/netex/network';
  }

  public getById(id: string): Observable<NetexNetwork> {
    return this.http.get<NetexNetwork>(this.networksUrl + "/" + id);
  }

  public findAll(): Observable<NetexNetwork[]> {
    return this.http.get<NetexNetwork[]>(this.networksUrl);
  }
}