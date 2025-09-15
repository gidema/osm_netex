import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Network } from './network';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class NetworkService {

  private readonly networksUrl = 'http://localhost:8080/network';
  private readonly http = inject(HttpClient);
  
  public getNetwork(id: string): Observable<Network> {
    return this.http.get<Network>(this.networksUrl + `/${id}`);
  }

  public findAll(): Observable<Network[]> {
    return this.http.get<Network[]>(this.networksUrl);
  }
}