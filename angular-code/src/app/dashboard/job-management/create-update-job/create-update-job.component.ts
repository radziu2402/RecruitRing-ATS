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
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "../model/location.model";

@Component({
  selector: 'app-create-update-job',
  standalone: true,
  templateUrl: './create-update-job.component.html',
  styleUrls: ['./create-update-job.component.scss'],
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
export class CreateUpdateJobComponent implements OnInit {

  offerCode: string | null = null;

  newJob: {
    titleId: number | null;
    locationId: number | null;
    jobCategoryId: number | null;
    workType: string;
    description: string;
    recruiterIds: number[];
    requirementIds: number[];
  } = {
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
    private readonly route: ActivatedRoute,
    private readonly jobService: JobService,
    private readonly titleService: TitleService,
    private readonly locationService: LocationService,
    private readonly categoryService: JobCategoryService,
    private readonly recruiterService: RecruiterService,
    private readonly requirementService: RequirementService,
    private readonly snackBar: MatSnackBar,
    private readonly router: Router
  ) {
  }

  ngOnInit(): void {

    this.offerCode = this.route.snapshot.paramMap.get('offerCode');

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

    const offerCode = this.route.snapshot.paramMap.get('offerCode');
    if (offerCode) {
      this.loadJobData(offerCode);
    }
  }

  addNewTitle = (term:string) =>{
    this.titleService.createTitle(term).subscribe((createdTitle: Title) => {
      this.availableTitles.push(createdTitle);
      this.availableTitles = this.availableTitles.slice(0);
      this.newJob.titleId = createdTitle.id;
      this.showNotification('Nowy tytuł został dodany!');
    });
    return true;
  }

  addNewLocation = (term: string) => {
    this.locationService.createLocation(term).subscribe((createdLocation: Location) => {
      this.availableLocations.push(createdLocation);
      this.newJob.locationId = createdLocation.id;
      this.showNotification('Nowa lokalizacja została dodana!');
    });
  }

  addNewCategory = (term: string) => {
    this.categoryService.createCategory(term).subscribe((createdCategory: JobCategory) => {
      this.availableCategories.push(createdCategory);
      this.newJob.jobCategoryId = createdCategory.id;
      this.showNotification('Nowa kategoria została dodana!');
    });
  }

  addNewRequirement = (term: string) => {
    this.requirementService.createRequirement(term).subscribe((createdRequirement: Requirement) => {
      this.availableRequirements.push(createdRequirement);
      this.newJob.requirementIds.push(createdRequirement.id);
      this.showNotification('Nowe wymaganie zostało dodane!');
    });
  }

  onSubmit(form: NgForm) {
    if (form.valid) {
      if (this.offerCode) {
        this.jobService.updateJob(this.offerCode, this.newJob).subscribe(() => {
          this.router.navigate(['/dashboard/jobs']).then(() => {
            this.showNotification('Oferta pracy została zaktualizowana pomyślnie!');
          });
        });
      } else {
        this.jobService.createJob(this.newJob).subscribe(() => {
          this.router.navigate(['/dashboard/jobs']).then(() => {
            this.showNotification('Oferta pracy została utworzona pomyślnie!');
          });
        });
      }
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

  loadJobData(offerCode: string) {
    this.jobService.getJobByOfferCode(offerCode).subscribe(job => {
      this.newJob.titleId = job.title.id;
      this.newJob.locationId = job.location.id;
      this.newJob.jobCategoryId = job.jobCategory.id;
      this.newJob.workType = job.workType;
      this.newJob.description = job.description;
      this.newJob.recruiterIds = job.recruiters.map(r => r.id);
      this.newJob.requirementIds = job.requirements.map(r => r.id);
    });
  }


}
