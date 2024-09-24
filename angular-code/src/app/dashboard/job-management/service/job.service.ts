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
  getJobById(id: number): Observable<JobPosting> {
    return this.http.get<JobPosting>(`${environment.api}${this.apiUrl}/${id}`);
  }

}
