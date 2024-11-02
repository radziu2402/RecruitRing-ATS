import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';
import {DetailedCandidateDTO} from "../model/detailed-candidate.model";

@Injectable({
  providedIn: 'root'
})
export class CandidateService {
  private readonly apiUrl = environment.api + 'recruitments/';

  constructor(private readonly http: HttpClient) {
  }

  getCandidatesByOfferCode(offerCode: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}${offerCode}/candidates`);
  }

  getCandidateDetails(applicationCode: string): Observable<DetailedCandidateDTO> {
    return this.http.get<DetailedCandidateDTO>(`${this.apiUrl}candidates/${applicationCode}`);
  }

  updateCandidate(candidate: DetailedCandidateDTO): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}candidates/${candidate.applicationCode}`, candidate);
  }

  getFileUrl(blobName: string): Observable<string> {
    const headers = {'X-Skip-Spinner': 'true'};
    return this.http.get<string>(`${environment.api}documents/download-url/${blobName}`,
      {
        responseType: 'text' as 'json',
        headers
      });
  }
}
