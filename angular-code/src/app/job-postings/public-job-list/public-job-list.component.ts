import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {PublicJobFilterComponent} from '../public-job-filter/public-job-filter.component';
import {PublicJobService} from '../service/public-job.service';
import {PublicJobSummaryPosting} from '../model/job-posting-summary.model';
import {ActivatedRoute, RouterLink} from "@angular/router";
import {JobFilterParams} from "../model/job-filter-param.model";

@Component({
  selector: 'app-public-job-list',
  standalone: true,
  imports: [
    NgForOf,
    PublicJobFilterComponent,
    NgIf,
    RouterLink
  ],
  templateUrl: './public-job-list.component.html',
  styleUrls: ['./public-job-list.component.scss']
})
export class PublicJobListComponent implements OnInit {
  jobs: PublicJobSummaryPosting[] = [];
  page = 0;
  pageSize = 10;
  totalJobs = 0;
  filters: JobFilterParams = {
    location: undefined,
    title: undefined,
    workType: '',
    jobCategory: undefined,
    searchTerm: ''
  };

  constructor(private readonly jobService: PublicJobService, private readonly route: ActivatedRoute) {
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

  loadJobs(): void {
    this.jobService.getAllJobPostings(this.page, this.pageSize, this.filters).subscribe(response => {
      this.jobs = response.content;
      this.totalJobs = response.totalElements;
    });
  }

  loadMore(): void {
    this.page++;
    this.loadJobs();
  }

  onJobsFiltered(newFilters: JobFilterParams): void {
    this.filters = newFilters;
    this.page = 0;
    this.loadJobs();
  }
}
