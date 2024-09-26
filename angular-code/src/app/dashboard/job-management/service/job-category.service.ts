import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class JobCategoryService {
  private readonly apiUrl = `${environment.api}job-categories`;

  constructor(private http: HttpClient) {}

  searchJobCategories(query: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/search`, { params: { query } });
  }
}
