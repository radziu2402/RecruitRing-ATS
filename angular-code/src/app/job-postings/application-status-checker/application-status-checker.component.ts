import {Component, LOCALE_ID, OnInit} from '@angular/core';
import { ApplicationStatusDTO } from "../model/application-status.model";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import { ApplicationService } from "../service/application.service";
import {DatePipe, NgClass, NgIf, registerLocaleData} from "@angular/common";
import { Router } from "@angular/router";
import { faArrowLeft } from "@fortawesome/free-solid-svg-icons";
import { FaIconComponent } from "@fortawesome/angular-fontawesome";
import { mapApplicationStatusToPolish } from "../../dashboard/recruitment/service/status-mapper";
import localePl from '@angular/common/locales/pl';

registerLocaleData(localePl);

@Component({
  selector: 'app-application-status-checker',
  standalone: true,
  imports: [
    NgIf,
    ReactiveFormsModule,
    DatePipe,
    FaIconComponent,
    NgClass
  ],
  providers: [{ provide: LOCALE_ID, useValue: 'pl-PL' }],
  templateUrl: './application-status-checker.component.html',
  styleUrl: './application-status-checker.component.scss'
})
export class ApplicationStatusCheckerComponent implements OnInit {
  applicationForm: FormGroup;
  applicationStatus: ApplicationStatusDTO | null = null;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private applicationService: ApplicationService,
    private router: Router
  ) {
    this.applicationForm = this.fb.group({
      applicationCode: ['', Validators.required]
    });
  }

  ngOnInit(): void {}

  get applicationCodeControl() {
    return this.applicationForm.get('applicationCode');
  }

  checkStatus() {
    if (this.applicationForm.valid) {
      const applicationCode = this.applicationForm.value.applicationCode;
      this.applicationService.getApplicationStatus(applicationCode).subscribe({
        next: (status: ApplicationStatusDTO) => {
          this.applicationStatus = status;
          this.errorMessage = '';
        },
        error: () => {
          this.errorMessage = 'Nieprawidłowy kod.';
          this.applicationStatus = null;
        }
      });
    } else {
      this.applicationCodeControl?.markAsTouched();
    }
  }

  goBack() {
    this.router.navigate(['/jobs']);
  }

  onKeyDownHandler(event: KeyboardEvent) {
    if (event.key === 'Enter' || event.key === ' ') {
      this.goBack();
    }
  }

  protected readonly faArrowLeft = faArrowLeft;
  protected readonly mapApplicationStatusToPolish = mapApplicationStatusToPolish;
}
