import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {JobPosting} from "../model/job-posting.model";
import {environment} from "../../../../environments/environment";
import {JobPostingSummary} from "../model/job-posting-summary.model";

@Injectable({
  providedIn: 'root'
})
export class JobService {
  private readonly apiUrl = 'jobs';

  constructor(private readonly http: HttpClient) {
  }

  getJobs(page: number, pageSize: number, skipSpinner: boolean = false): Observable<{ content: JobPostingSummary[], totalElements: number }> {
    const headers = skipSpinner ? new HttpHeaders({ 'X-Skip-Spinner': 'true' }) : undefined;

    return this.http.get<{ content: JobPostingSummary[], totalElements: number }>(
      `${environment.api}${this.apiUrl}?page=${page}&size=${pageSize}`,
      { headers }
    );
  }

  getJobByOfferCode(offerCode: string): Observable<JobPosting> {
    return this.http.get<JobPosting>(`${environment.api}${this.apiUrl}/${offerCode}`);
  }

  createJob(jobData: any): Observable<JobPosting> {
    return this.http.post<JobPosting>(`${environment.api}${this.apiUrl}`, jobData);
  }

  deleteJobByOfferCode(offerCode: string): Observable<void> {
    return this.http.delete<void>(`${environment.api}${this.apiUrl}/${offerCode}`);
  }

  updateJob(offerCode: string, updatedJob: any): Observable<any> {
    return this.http.put(`${environment.api}${this.apiUrl}/${offerCode}`, updatedJob);
  }

}
