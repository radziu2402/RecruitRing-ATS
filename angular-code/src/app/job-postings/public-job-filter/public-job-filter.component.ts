import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgForOf} from '@angular/common';
import {PublicJobSummaryPosting} from '../model/job-posting-summary.model';
import {TitleService} from "../../dashboard/job-management/service/title.service";
import {LocationService} from "../../dashboard/job-management/service/location.service";
import {JobCategoryService} from "../../dashboard/job-management/service/job-category.service";
import {JobFilterParams} from "../model/job-filter-param.model";
import {JobCategory} from "../../dashboard/job-management/model/job-category.model";
import {Location} from "../../dashboard/job-management/model/location.model";
import {Title} from "../../dashboard/job-management/model/title.model";

@Component({
  selector: 'app-public-job-filter',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf
  ],
  templateUrl: './public-job-filter.component.html',
  styleUrls: ['./public-job-filter.component.scss']
})
export class PublicJobFilterComponent implements OnInit {
  @Input() jobs: PublicJobSummaryPosting[] = [];
  @Output() filteredJobs = new EventEmitter<JobFilterParams>();

  locations: Location[] = [];
  titles: Title[] = [];
  workTypes: string[] = ['STATIONARY', 'REMOTE', 'HYBRID'];
  jobCategories: JobCategory[] = [];

  selectedLocation: Location | null = null;
  selectedTitle: Title | null = null;
  selectedWorkType: string = '';
  selectedJobCategory: JobCategory | null = null;
  searchTerm: string = '';

  constructor(
    private readonly titleService: TitleService,
    private readonly locationService: LocationService,
    private readonly categoryService: JobCategoryService,
  ) {
  }

  ngOnInit() {
    this.titleService.searchTitles('').subscribe(titles => this.titles = titles);
    this.locationService.searchLocations('').subscribe(locations => this.locations = locations);
    this.categoryService.searchJobCategories('').subscribe(categories => this.jobCategories = categories);
    this.workTypes = ['STATIONARY', 'REMOTE', 'HYBRID'];
  }

  onFilterChange() {
    const filterParams: JobFilterParams = {
      location: this.selectedLocation ? +this.selectedLocation.id : undefined,
      title: this.selectedTitle ? +this.selectedTitle.id : undefined,
      workType: this.selectedWorkType,
      jobCategory: this.selectedJobCategory ? +this.selectedJobCategory.id : undefined, // Wyodrębnij id jako liczba
      searchTerm: this.searchTerm,
    };

    this.filteredJobs.emit(filterParams);
  }
}
