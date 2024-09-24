import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {JobPosting} from "../model/job-posting.model";
import {JobService} from "../service/job.service";
import {NgForOf} from "@angular/common";


@Component({
  selector: 'app-job-details',
  templateUrl: './job-detail.component.html',
  standalone: true,
  imports: [
    NgForOf
  ],
  styleUrls: ['./job-detail.component.scss']
})
export class JobDetailComponent implements OnInit {
  job!: JobPosting;

  constructor(
    private route: ActivatedRoute,
    private jobService: JobService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const jobId = this.route.snapshot.paramMap.get('id');
    if (jobId) {
      this.jobService.getJobById(+jobId).subscribe((job) => {
        this.job = job;
      });
    }
  }

  formatDate(dateArray: [number, number, number]): string {
    const [year, month, day] = dateArray;
    return `${day}-${month}-${year}`;
  }

  goBack() {
    this.router.navigate(['/dashboard/jobs']);
  }
}
