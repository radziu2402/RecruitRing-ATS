import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {JobPosting} from "../model/job-posting.model";
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class JobService {
  private readonly apiUrl = 'jobs';

  constructor(private http: HttpClient) {
  }

  getJobs(page: number, pageSize: number): Observable<{ content: JobPosting[], totalElements: number }> {
    return this.http.get<{ content: JobPosting[], totalElements: number }>(
      `${environment.api}${this.apiUrl}?page=${page}&size=${pageSize}`
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
