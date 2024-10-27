import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RecruitmentService {
  private readonly apiUrl = '/api/v1/jobs/recruiter/assigned';

  constructor(private readonly http: HttpClient) {}

  getRecruitments(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}
