import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {PublicJobFilterComponent} from '../public-job-filter/public-job-filter.component';
import {PublicJobService} from '../service/public-job.service';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {JobFilterParams} from "../model/job-filter-param.model";
import {mapWorkType} from "../service/work-type-mapper";
import {PublicJobSummaryPosting} from "../model/public-job-posting-summary.model";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faChartSimple} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-public-job-list',
  standalone: true,
  imports: [
    NgForOf,
    PublicJobFilterComponent,
    NgIf,
    RouterLink,
    FaIconComponent
  ],
  templateUrl: './public-job-list.component.html',
  styleUrls: ['./public-job-list.component.scss']
})
export class PublicJobListComponent implements OnInit {
  jobs: PublicJobSummaryPosting[] = [];
  filteredJobs: PublicJobSummaryPosting[] = [];
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

  constructor(private readonly jobService: PublicJobService, private readonly route: ActivatedRoute, private readonly router: Router) {
  }

  ngOnInit(): void {
    this.route.data.subscribe((data) => {
      this.jobs = data['jobs'].content;
      this.filteredJobs = this.jobs;
      this.totalJobs = data['jobs'].totalElements;
    });
  }

  formatDate(dateArray: [number, number, number]): string {
    const [year, month, day] = dateArray;
    return `${day}-${month}-${year}`;
  }

  loadJobs(): void {
    this.jobService.getAllJobPostings(this.page, this.pageSize, this.filters).subscribe(response => {
      this.jobs = [...this.jobs, ...response.content];
      this.filteredJobs = this.jobs;
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
    this.jobs = [];
    this.loadJobs();
  }

  onSearchTermChange(searchTerm: string): void {
    const searchTermLower = searchTerm.toLowerCase();
    this.filteredJobs = this.jobs.filter(job => {
      return (
        job.title.toLowerCase().includes(searchTermLower) ||
        job.location.toLowerCase().includes(searchTermLower) ||
        job.jobCategory.toLowerCase().includes(searchTermLower) ||
        job.workType.toLowerCase().includes(searchTermLower)
      );
    });
  }

  protected readonly mapWorkType = mapWorkType;

  openApplicationStatusChecker(): void {
    this.router.navigate(['jobs/status']);
  }

  protected readonly faChartSimple = faChartSimple;
}
