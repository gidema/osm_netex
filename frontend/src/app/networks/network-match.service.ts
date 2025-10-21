import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import NetworkMatch from '@networks/network-match';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export default class NetworkMatchService {

  private readonly networksUrl = 'http://localhost:8080/network';
  private readonly http = inject(HttpClient);
  
  public findById(id: string): Observable<NetworkMatch> {
    return this.http.get<NetworkMatch>(this.networksUrl + `/${id}`);
  }

  public findAll(): Observable<NetworkMatch[]> {
    return this.http.get<NetworkMatch[]>(this.networksUrl);
  }
}