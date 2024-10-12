import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgForOf} from '@angular/common';
import {TitleService} from "../../dashboard/job-management/service/title.service";
import {LocationService} from "../../dashboard/job-management/service/location.service";
import {JobCategoryService} from "../../dashboard/job-management/service/job-category.service";
import {JobFilterParams} from "../model/job-filter-param.model";
import {JobCategory} from "../../dashboard/job-management/model/job-category.model";
import {Location} from "../../dashboard/job-management/model/location.model";
import {Title} from "../../dashboard/job-management/model/title.model";
import {mapWorkType} from "../service/work-type-mapper";
import {PublicJobSummaryPosting} from "../model/public-job-posting-summary.model";

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
  @Output() searchTermChanged = new EventEmitter<string>();

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
      workType: this.selectedWorkType ? this.selectedWorkType : undefined,
      jobCategory: this.selectedJobCategory ? +this.selectedJobCategory.id : undefined,
      searchTerm: this.searchTerm ? this.searchTerm.trim() : undefined,
    };

    this.filteredJobs.emit(filterParams);
  }

  onSearchTermChange() {
    this.searchTermChanged.emit(this.searchTerm);
  }

  resetFilters() {
    this.selectedLocation = null;
    this.selectedTitle = null;
    this.selectedWorkType = '';
    this.selectedJobCategory = null;
    this.searchTerm = '';
    this.onFilterChange();
  }

  protected readonly mapWorkType = mapWorkType;
}
