import {Component, OnInit} from '@angular/core';
import {JobService} from '../service/job.service';
import {RecruiterService} from '../service/recruiter.service';
import {RequirementService} from '../service/requirement.service';
import {Subject} from 'rxjs';
import {debounceTime, distinctUntilChanged, switchMap} from 'rxjs/operators';
import {TitleService} from "../service/title.service";
import {LocationService} from "../service/location.service";
import {JobCategoryService} from "../service/job-category.service";
import {Title} from "../model/title.model";
import {JobCategory} from "../model/job-category.model";
import {Recruiter} from "../model/recruiter.model";
import {Requirement} from "../model/requirement.model";
import {
  NgLabelTemplateDirective,
  NgOptionTemplateDirective,
  NgSelectComponent,
  NgTagTemplateDirective
} from '@ng-select/ng-select';
import {FormsModule, NgForm} from '@angular/forms';
import {NgClass, NgIf} from "@angular/common";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-job',
  standalone: true,
  templateUrl: './create-job.component.html',
  styleUrls: ['./create-job.component.scss'],
  imports: [
    FormsModule,
    NgLabelTemplateDirective,
    NgOptionTemplateDirective,
    NgSelectComponent,
    NgClass,
    NgIf,
    NgTagTemplateDirective
  ]
})
export class CreateJobComponent implements OnInit {

  newJob = {
    titleId: null,
    locationId: null,
    jobCategoryId: null,
    workType: 'STATIONARY',
    description: '',
    recruiterIds: [],
    requirementIds: []
  };

  availableTitles: Title[] = [];
  availableLocations: Location[] = [];
  availableCategories: JobCategory[] = [];
  availableRecruiters: Recruiter[] = [];
  availableRequirements: Requirement[] = [];

  titleInput$ = new Subject<string>();
  locationInput$ = new Subject<string>();
  categoryInput$ = new Subject<string>();
  recruiterInput$ = new Subject<string>();
  requirementInput$ = new Subject<string>();

  constructor(
    private jobService: JobService,
    private titleService: TitleService,
    private locationService: LocationService,
    private categoryService: JobCategoryService,
    private recruiterService: RecruiterService,
    private requirementService: RequirementService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    // Load initial titles
    this.titleService.searchTitles('').subscribe(titles => this.availableTitles = titles);

    // Set up typeahead for titles
    this.titleInput$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => this.titleService.searchTitles(term))
    ).subscribe(titles => this.availableTitles = titles);

    // Load initial locations
    this.locationService.searchLocations('').subscribe(locations => this.availableLocations = locations);

    // Set up typeahead for locations
    this.locationInput$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => this.locationService.searchLocations(term))
    ).subscribe(locations => this.availableLocations = locations);

    // Load initial categories
    this.categoryService.searchJobCategories('').subscribe(categories => this.availableCategories = categories);

    // Set up typeahead for categories
    this.categoryInput$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => this.categoryService.searchJobCategories(term))
    ).subscribe(categories => this.availableCategories = categories);

    this.recruiterService.searchRecruiters('').subscribe(recruiters => {
      this.availableRecruiters = this.mapRecruitersWithFullName(recruiters);
    });

    // Set up typeahead for recruiters
    this.recruiterInput$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => this.recruiterService.searchRecruiters(term))
    ).subscribe(recruiters => {
      this.availableRecruiters = this.mapRecruitersWithFullName(recruiters);
    });

    // Load initial requirements
    this.requirementService.searchRequirements('').subscribe(requirements => this.availableRequirements = requirements);

    // Set up typeahead for requirements
    this.requirementInput$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => this.requirementService.searchRequirements(term))
    ).subscribe(requirements => this.availableRequirements = requirements);
  }

  onSubmit(form: NgForm) {
    if (form.valid) {
      this.jobService.createJob(this.newJob).subscribe(() => {
        this.router.navigate(['/dashboard/jobs']).then(() => {
          this.showNotification('Oferta pracy została utworzona pomyślnie!');
        });
      });
    } else {
      this.showNotification('Wypełnij wszystkie wymagane pola przed zapisaniem!');
      this.markFormFieldsAsTouched(form);
    }
  }

  private mapRecruitersWithFullName(recruiters: Recruiter[]): Recruiter[] {
    return recruiters.map(recruiter => ({
      ...recruiter,
      fullName: `${recruiter.firstName} ${recruiter.lastName}`
    }));
  }

  private markFormFieldsAsTouched(form: NgForm) {
    Object.values(form.controls).forEach(control => {
      control.markAsTouched();
    });
  }

  showNotification(message: string) {
    this.snackBar.open(message, 'Zamknij', {
      duration: 3000,
    });
  }

}
