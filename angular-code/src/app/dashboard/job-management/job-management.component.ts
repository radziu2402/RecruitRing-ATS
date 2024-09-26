import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {JobPosting} from "./model/job-posting.model";
import {JobService} from "./service/job.service";
import {NgForOf} from "@angular/common";


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
  jobs: JobPosting[] = [];
  page = 0;
  pageSize = 10;
  totalJobs = 0;

  constructor(private route: ActivatedRoute, private jobService: JobService, private router: Router) {
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
    this.jobService.getJobs(this.page, this.pageSize).subscribe((response) => {
      this.jobs = [...this.jobs, ...response.content];
    });
  }
}
