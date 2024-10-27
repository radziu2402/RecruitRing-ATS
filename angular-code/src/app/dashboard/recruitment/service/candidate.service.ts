import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CandidateService {
  private readonly apiUrl = environment.api + 'recruitments/';

  constructor(private readonly http: HttpClient) {}

  getCandidatesByOfferCode(offerCode: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}${offerCode}/candidates`);
  }
}
