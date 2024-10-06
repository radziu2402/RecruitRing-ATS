import {Component, Input} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";

@Component({
  selector: 'app-public-job-application-form',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './public-job-application-form.component.html',
  styleUrl: './public-job-application-form.component.scss'
})
export class PublicJobApplicationFormComponent {
  @Input() job: any;
  applicationForm: FormGroup;

  constructor(private readonly fb: FormBuilder) {
    this.applicationForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      resume: ['', Validators.required],
      coverLetter: ['']
    });
  }

  // Metoda obsługi wysłania formularza
  submitApplication() {
    if (this.applicationForm.valid) {
      // Logika wysyłania aplikacji
    }
  }
}
