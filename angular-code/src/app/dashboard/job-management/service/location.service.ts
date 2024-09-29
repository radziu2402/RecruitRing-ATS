import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../../../environments/environment";
import {Location} from "../model/location.model";

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private readonly apiUrl = `${environment.api}locations`;

  constructor(private http: HttpClient) {}

  searchLocations(query: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/search`, { params: { query } });
  }

  createLocation(location: string): Observable<Location> {
    return this.http.post<Location>(this.apiUrl, location);
  }
}
