import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NgForOf, NgIf} from '@angular/common';
import {Recruitment} from "../model/recruitment.model";
import {mapWorkType} from "../../../job-postings/service/work-type-mapper";

@Component({
  selector: 'app-recruitment-list',
  standalone: true,
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './recruitment-list.component.html',
  styleUrls: ['./recruitment-list.component.scss']
})
export class RecruitmentListComponent implements OnInit {
  recruitments: Recruitment[] = [];
  filteredRecruitments: Recruitment[] = [];

  showFilterOptions: { [key in 'jobCategory' | 'location' | 'workType']: boolean } = {
    jobCategory: false,
    location: false,
    workType: false
  };

  activeFilters: { jobCategory: string | null; location: string | null; workType: string | null } = {
    jobCategory: null,
    location: null,
    workType: null
  };

  uniqueJobCategories: string[] = [];
  uniqueLocations: string[] = [];
  uniqueWorkTypes: string[] = [];

  constructor(private readonly router: Router, private readonly route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.recruitments = data['recruitments'];
      this.filteredRecruitments = [...this.recruitments];

      this.uniqueJobCategories = [...new Set(this.recruitments.map(r => r.jobCategory))];
      this.uniqueLocations = [...new Set(this.recruitments.map(r => r.location))];
      this.uniqueWorkTypes = [...new Set(this.recruitments.map(r => r.workType))];
    });
  }

  applyFilter(filterType: keyof typeof this.activeFilters, value: string) {
    this.activeFilters[filterType] = this.activeFilters[filterType] === value ? null : value;
    this.filterRecruitments();
  }

  filterRecruitments() {
    this.filteredRecruitments = this.recruitments.filter(r =>
      (!this.activeFilters.jobCategory || r.jobCategory === this.activeFilters.jobCategory) &&
      (!this.activeFilters.location || r.location === this.activeFilters.location) &&
      (!this.activeFilters.workType || r.workType === this.activeFilters.workType)
    );
  }

  toggleFilterOptions(filterType: keyof typeof this.showFilterOptions) {
    this.showFilterOptions[filterType] = !this.showFilterOptions[filterType];
  }

  onSearch(event: any) {
    const query = event.target.value.toLowerCase();
    this.filteredRecruitments = this.recruitments.filter(r =>
      r.title.toLowerCase().includes(query) ||
      r.location.toLowerCase().includes(query)
    );
  }

  resetFilters() {
    this.activeFilters = {jobCategory: '', location: '', workType: ''};
    this.filteredRecruitments = [...this.recruitments];
  }

  goToRecruitment(offerCode: string, offerName: string) {
    this.router.navigate(['/dashboard/recruitment', offerCode], { queryParams: { offerName } });
  }

  protected readonly mapWorkType = mapWorkType;

  onKeyDownHandler(event: KeyboardEvent, offerCode: string, title: string): void {
    if (event.key === 'Enter' || event.key === ' ') {
      this.goToRecruitment(offerCode, title);
    }
  }

  onKeyDownToggleFilter(event: KeyboardEvent, filterType: keyof typeof this.showFilterOptions): void {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.toggleFilterOptions(filterType);
    }
  }

  onKeyDownApplyFilter(event: KeyboardEvent, filterType: keyof typeof this.activeFilters, value: string): void {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.applyFilter(filterType, value);
    }
  }

}
