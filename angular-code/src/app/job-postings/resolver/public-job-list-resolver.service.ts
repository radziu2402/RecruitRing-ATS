import {Injectable} from '@angular/core';
import {Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {PublicJobService} from "../service/public-job.service";
import {PublicJobSummaryPosting} from "../model/job-posting-summary.model";

@Injectable({
  providedIn: 'root'
})
export class PublicJobListResolver implements Resolve<{ content: PublicJobSummaryPosting[], totalElements: number }> {

  constructor(private readonly jobService: PublicJobService) {
  }

  resolve(): Observable<{ content: PublicJobSummaryPosting[], totalElements: number }> {
    const page = 0;
    const pageSize = 10;

    return this.jobService.getAllJobPostings(page, pageSize, {});
  }
}
