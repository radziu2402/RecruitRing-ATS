import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgClass, NgIf} from "@angular/common";
import {AuthService} from "../../core/service/security/auth.service";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faArrowLeft, faCheckCircle} from '@fortawesome/free-solid-svg-icons';
import {ActivatedRoute, Router} from "@angular/router";
import {Address, ApplicationDTO} from "../model/application.model";
import {ApplicationService} from "../service/application.service";

@Component({
  selector: 'app-public-job-application-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    FormsModule,
    NgClass,
    FaIconComponent
  ],
  templateUrl: './public-job-application-form.component.html',
  styleUrl: './public-job-application-form.component.scss'
})
export class PublicJobApplicationFormComponent implements OnInit {
  applicationForm: FormGroup;
  jobTitle: string | null = null;
  offerCode: string | null = null;
  showVerificationModal: boolean = false;
  isEmailVerified: boolean = false;
  selectedFile: File | null = null;
  verificationError: string = '';
  faCheckCircle = faCheckCircle;

  constructor(private readonly fb: FormBuilder,
              private readonly authService: AuthService,
              private readonly router: Router,
              private readonly route: ActivatedRoute,
              private readonly applicationService: ApplicationService) {
    this.applicationForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      phone: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      confirmEmail: ['', [Validators.required, Validators.email]],
      city: ['', Validators.required],
      postCode: ['', Validators.required],
      street: [''],
      streetNumber: [null],
      flatNumber: [null],
      resume: [null, Validators.required],
      verificationCode: ['', Validators.required]
    }, {validators: this.emailMatchValidator});
  }

  ngOnInit(): void {
    const state = window.history.state;
    this.jobTitle = state?.jobTitle;
    this.offerCode = this.route.snapshot.paramMap.get('offerCode');
  }

  emailMatchValidator(group: FormGroup): any {
    const email = group.get('email')?.value;
    const confirmEmail = group.get('confirmEmail')?.value;
    return email === confirmEmail ? null : {emailsNotMatching: true};
  }

  areEmailsMatching(): boolean {
    const email = this.applicationForm.get('email')?.value;
    const confirmEmail = this.applicationForm.get('confirmEmail')?.value;
    return email === confirmEmail && email !== '' && confirmEmail !== '';
  }

  verifyEmails(): void {
    const emailControl = this.applicationForm.get('email');
    const confirmEmailControl = this.applicationForm.get('confirmEmail');

    if (!this.areEmailsMatching()) {
      emailControl?.markAsTouched();
      confirmEmailControl?.markAsTouched();
    } else {
      this.openVerificationModal();
    }
  }

  openVerificationModal() {
    const email = this.applicationForm.get('email')?.value;
    if (email) {
      this.showVerificationModal = true;
      this.authService.sendVerificationCode(email).subscribe();
    }
  }

  closeVerificationModal() {
    this.showVerificationModal = false;
  }

  verifyCode() {
    if (this.applicationForm.get('verificationCode')?.invalid) {
      this.applicationForm.get('verificationCode')?.markAsTouched();
      return;
    }

    const email = this.applicationForm.get('email')?.value;
    const verificationCode = this.applicationForm.get('verificationCode')?.value;

    this.authService.confirmVerificationCode(email, verificationCode).subscribe({
      next: () => {
        this.isEmailVerified = true;
        this.applicationForm.get('email')?.disable();
        this.applicationForm.get('confirmEmail')?.disable();
        this.closeVerificationModal(); // Zamknij modal
        this.verificationError = '';

      },
      error: () => {
        this.verificationError = 'Wystąpił błąd podczas weryfikacji kodu.';
      }
    });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  submitApplication(): void {
    if (this.applicationForm.valid && this.isEmailVerified) {
      const applicationData = this.createApplicationDto();
      const formData = new FormData();
      formData.append('application', new Blob([JSON.stringify(applicationData)], {type: 'application/json'}));
      formData.append('cv', this.selectedFile as Blob);
      this.applicationService.submitApplication(formData).subscribe({
        next: () => this.router.navigate([`/jobs/apply/${this.applicationForm.get('offerCode')?.value}/confirmation`])
      });
    }
  }

  createApplicationDto(): ApplicationDTO {
    const address: Address = {
      city: this.applicationForm.get('city')?.value,
      postCode: this.applicationForm.get('postCode')?.value,
      street: this.applicationForm.get('street')?.value,
      streetNumber: this.applicationForm.get('streetNumber')?.value,
      flatNumber: this.applicationForm.get('flatNumber')?.value
    };

    return {
      firstName: this.applicationForm.get('firstName')?.value,
      lastName: this.applicationForm.get('lastName')?.value,
      phone: this.applicationForm.get('phone')?.value,
      email: this.applicationForm.get('email')?.value,
      offerCode: this.offerCode as string,
      address: address
    };
  }

  goBack() {
    this.router.navigate(['/jobs', this.offerCode]);
  }

  onKeyDownHandler(event: KeyboardEvent) {
    if (event.key === 'Enter' || event.key === ' ') {
      this.goBack();
    }
  }

  protected readonly faArrowLeft = faArrowLeft;
}
