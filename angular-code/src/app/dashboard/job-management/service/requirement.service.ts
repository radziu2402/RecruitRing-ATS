import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from "../../../../environments/environment";
import {Requirement} from "../model/requirement.model";

@Injectable({
  providedIn: 'root'
})
export class RequirementService {
  private readonly apiUrl = `${environment.api}requirements`;

  constructor(private readonly http: HttpClient) {
  }

  searchRequirements(query: string): Observable<any[]> {
    const headers = { 'X-Skip-Spinner': 'true' };
    return this.http.get<any[]>(`${this.apiUrl}/search`, { params: { query }, headers });
  }

  createRequirement(requirement: string): Observable<Requirement> {
    const headers = { 'X-Skip-Spinner': 'true' };
    return this.http.post<Requirement>(this.apiUrl, requirement, { headers });
  }
}
