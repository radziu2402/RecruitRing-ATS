import {Injectable} from '@angular/core';
import {Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {JobService} from "../service/job.service";
import {JobPosting} from "../model/job-posting.model";

@Injectable({
  providedIn: 'root'
})
export class JobManagementResolver implements Resolve<{ content: JobPosting[], totalElements: number }> {

  constructor(private readonly jobService: JobService) {
  }

  resolve(): Observable<{ content: JobPosting[], totalElements: number }> {
    const page = 0;
    const pageSize = 10;

    return this.jobService.getJobs(page, pageSize);
  }
}
