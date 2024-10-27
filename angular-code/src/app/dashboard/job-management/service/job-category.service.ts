import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../../../environments/environment";
import {JobCategory} from "../model/job-category.model";

@Injectable({
  providedIn: 'root'
})
export class JobCategoryService {
  private readonly apiUrl = `${environment.api}job-categories`;

  constructor(private readonly http: HttpClient) {}

  searchJobCategories(query: string): Observable<any[]> {
    const headers = { 'X-Skip-Spinner': 'true' };
    return this.http.get<any[]>(`${this.apiUrl}/search`, { params: { query }, headers });
  }

  createCategory(category: string): Observable<JobCategory> {
    const headers = { 'X-Skip-Spinner': 'true' };
    return this.http.post<JobCategory>(this.apiUrl, category, { headers });
  }
}
