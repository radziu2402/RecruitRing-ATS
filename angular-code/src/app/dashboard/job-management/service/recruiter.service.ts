import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class RecruiterService {
  private readonly apiUrl = `${environment.api}recruiters`;

  constructor(private readonly http: HttpClient) {
  }

  searchRecruiters(query: string): Observable<any[]> {
    const headers = { 'X-Skip-Spinner': 'true' };
    return this.http.get<any[]>(`${this.apiUrl}/search`, { params: { query }, headers });
  }
}
