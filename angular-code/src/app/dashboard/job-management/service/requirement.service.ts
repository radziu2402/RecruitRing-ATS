import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class RequirementService {
  private readonly apiUrl = `${environment.api}requirements`;

  constructor(private http: HttpClient) {
  }

  searchRequirements(query: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/search`, {params: {query}});
  }
}
