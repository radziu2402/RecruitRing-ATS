import { Component, OnInit } from '@angular/core';
import { NgForOf, NgIf } from "@angular/common";
import { CandidateService } from "../service/candidate.service";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: 'app-recruitment-management',
  standalone: true,
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './recruitment-management.component.html',
  styleUrls: ['./recruitment-management.component.scss']
})
export class RecruitmentManagementComponent implements OnInit {
  candidates: any[] = [];
  filteredCandidates: any[] = [];

  showFilterOptions: { [key: string]: boolean } = { status: false, location: false };
  activeFilters: { status: string | null; location: string | null } = { status: null, location: null };
  uniqueStatuses: string[] = [];
  uniqueLocations: string[] = [];
  offerCode: string | null = null;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly candidateService: CandidateService
  ) {}

  ngOnInit(): void {
    this.offerCode = this.route.snapshot.paramMap.get('offerCode');
    if (this.offerCode) {
      this.loadCandidates(this.offerCode);
    }
  }

  loadCandidates(offerCode: string) {
    this.candidateService.getCandidatesByOfferCode(offerCode).subscribe((candidates) => {
      this.candidates = candidates;
      this.filteredCandidates = candidates;
      this.extractUniqueFilterOptions();
    });
  }

  extractUniqueFilterOptions() {
    this.uniqueStatuses = Array.from(new Set(this.candidates.map(c => c.status)));
    this.uniqueLocations = Array.from(new Set(this.candidates.map(c => c.location)));
  }

  applyFilter(filterType: 'status' | 'location', value: string) {
    this.activeFilters[filterType] = value;
    this.filterCandidates();
  }

  filterCandidates() {
    this.filteredCandidates = this.candidates.filter(c =>
      (!this.activeFilters.status || c.status === this.activeFilters.status) &&
      (!this.activeFilters.location || c.location === this.activeFilters.location)
    );
  }

  toggleFilterOptions(filterType: 'status' | 'location') {
    this.showFilterOptions[filterType] = !this.showFilterOptions[filterType];
  }

  resetFilters() {
    this.activeFilters = { status: null, location: null };
    this.filteredCandidates = [...this.candidates];
  }

  onSearch(event: Event) {
    const query = (event.target as HTMLInputElement).value.toLowerCase();
    this.filteredCandidates = this.candidates.filter(c =>
      c.name.toLowerCase().includes(query) ||
      c.email.toLowerCase().includes(query) ||
      c.phone.toLowerCase().includes(query)
    );
  }
}
