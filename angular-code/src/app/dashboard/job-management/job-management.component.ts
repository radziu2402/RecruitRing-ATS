import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {JobService} from "./service/job.service";
import {NgForOf} from "@angular/common";
import {JobPostingSummary} from "./model/job-posting-summary.model";
import {mapWorkType} from "../../job-postings/service/work-type-mapper";


@Component({
  selector: 'app-job-management',
  standalone: true,
  templateUrl: './job-management.component.html',
  styleUrls: ['./job-management.component.scss'],
  imports: [
    NgForOf,
    RouterLink
  ]
})
export class JobManagementComponent implements OnInit {
  jobs: JobPostingSummary[] = [];
  page = 0;
  pageSize = 10;
  totalJobs = 0;

  constructor(private readonly route: ActivatedRoute, private readonly jobService: JobService, private readonly router: Router) {
  }

  ngOnInit(): void {
    this.route.data.subscribe((data) => {
      this.jobs = data['jobs'].content;
      this.totalJobs = data['jobs'].totalElements;
    });
  }

  formatDate(dateArray: [number, number, number]): string {
    const [year, month, day] = dateArray;
    return `${day}-${month}-${year}`;
  }

  navigateToCreateJob(): void {
    this.router.navigate(['/dashboard/create-job']);
  }

  loadMore(): void {
    this.page++;
    this.jobService.getJobs(this.page, this.pageSize, true).subscribe((response) => {
      this.jobs = [...this.jobs, ...response.content];
    });
  }

  protected readonly mapWorkType = mapWorkType;
}
