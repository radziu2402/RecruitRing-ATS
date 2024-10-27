import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class RecruitmentService {
  private readonly apiUrl = environment.api + 'jobs/recruiter/assigned';

  constructor(private readonly http: HttpClient) {}

  getRecruitments(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}
