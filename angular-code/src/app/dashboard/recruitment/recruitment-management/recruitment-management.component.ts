import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {LowerCasePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {CandidateService} from '../service/candidate.service';
import {faArrowLeft} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {mapApplicationStatusToPolish} from "../service/status-mapper";

@Component({
  selector: 'app-recruitment-management',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    FaIconComponent,
    NgClass,
    LowerCasePipe
  ],
  templateUrl: './recruitment-management.component.html',
  styleUrls: ['./recruitment-management.component.scss']
})
export class RecruitmentManagementComponent implements OnInit {
  offerName: string | null = null;
  offerCode: string | null = null;
  candidates: any[] = [];
  filteredCandidates: any[] = [];

  showFilterOptions: { [key: string]: boolean } = {
    status: false,
    location: false,
    rating: false
  };

  activeFilters: { status: string | null; location: string | null; rating: number | null } = {
    status: null,
    location: null,
    rating: null
  };

  uniqueStatuses: string[] = [];
  uniqueLocations: string[] = [];
  uniqueRatings: (number | string)[] = ['Nieocenione', 1, 2, 3, 4, 5];

  constructor(
    private readonly route: ActivatedRoute,
    private readonly candidateService: CandidateService,
    private readonly router: Router
  ) {
  }

  ngOnInit(): void {
    this.offerCode = this.route.snapshot.paramMap.get('offerCode');
    if (this.offerCode) {
      this.loadCandidates(this.offerCode);
    }
    this.route.queryParams.subscribe(params => {
      this.offerName = params['offerName'];
    });
  }

  loadCandidates(offerCode: string): void {
    this.candidateService.getCandidatesByOfferCode(offerCode).subscribe(candidates => {
      this.candidates = candidates.map(candidate => ({
        ...candidate,
        statusPolish: mapApplicationStatusToPolish(candidate.status),
        ratingDisplay: candidate.rating === 0 ? 'Nieocenione' : candidate.rating
      }));
      this.filteredCandidates = [...this.candidates];
      this.extractUniqueFilterOptions();
    });
  }

  extractUniqueFilterOptions(): void {
    this.uniqueStatuses = Array.from(new Set(this.candidates.map(c => c.statusPolish)));
    this.uniqueLocations = Array.from(new Set(this.candidates.map(c => c.city)));
  }

  applyFilter(filterType: 'status' | 'location' | 'rating', value: any): void {
    if (filterType === 'rating') {
      this.activeFilters.rating = value === 'Nieocenione' ? 0 : value;
    } else {
      this.activeFilters[filterType] = this.activeFilters[filterType] === value ? null : value;
    }
    this.filterCandidates();
  }

  filterCandidates(): void {
    this.filteredCandidates = this.candidates.filter(candidate =>
      (!this.activeFilters.status || candidate.statusPolish === this.activeFilters.status) &&
      (!this.activeFilters.location || candidate.city === this.activeFilters.location) &&
      (this.activeFilters.rating === null || candidate.rating === this.activeFilters.rating)
    );
  }

  toggleFilterOptions(filterType: 'status' | 'location' | 'rating'): void {
    this.showFilterOptions[filterType] = !this.showFilterOptions[filterType];
  }

  resetFilters(): void {
    this.activeFilters = {status: null, location: null, rating: null};
    this.filteredCandidates = [...this.candidates];
  }

  onSearch(event: Event): void {
    const query = (event.target as HTMLInputElement).value.toLowerCase();
    this.filteredCandidates = this.candidates.filter(candidate =>
      candidate.firstName.toLowerCase().includes(query) ||
      candidate.lastName.toLowerCase().includes(query) ||
      candidate.email.toLowerCase().includes(query) ||
      candidate.phone.includes(query) ||
      candidate.city.toLowerCase().includes(query)
    );
  }

  goBack() {
    this.router.navigate(['/dashboard/recruitment']);
  }

  onKeyDownHandler(event: KeyboardEvent) {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.goBack();
    }
  }

  onKeyDownToggleFilter(event: KeyboardEvent, filterType: 'status' | 'location' | 'rating'): void {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.toggleFilterOptions(filterType);
    }
  }

  onKeyDownApplyFilter(event: KeyboardEvent, filterType: 'status' | 'location' | 'rating', value: any): void {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.applyFilter(filterType, value);
    }
  }

  onKeyDownGoToCandidate(event: KeyboardEvent, applicationCode: string) {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.goToCandidateDetails(applicationCode);
    }
  }

  goToCandidateDetails(applicationCode: string) {
    this.router.navigate(['/dashboard/recruitment', this.offerCode, 'candidate', applicationCode]);
  }

  protected readonly faArrowLeft = faArrowLeft;
  protected readonly mapApplicationStatusToPolish = mapApplicationStatusToPolish;
}
