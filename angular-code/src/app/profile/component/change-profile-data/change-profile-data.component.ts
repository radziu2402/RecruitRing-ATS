import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ReactiveFormsModule, UntypedFormBuilder, UntypedFormControl, Validators} from '@angular/forms';
import {User} from '../../model/user';
import {ProfileService} from '../../service/profile.service';
import {BaseFormComponent} from '../../../core/base-form/base-form.component';
import {NgIf} from "@angular/common";
import {MatButton} from "@angular/material/button";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faArrowLeft} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'change-profile-data',
  templateUrl: './change-profile-data.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    MatButton,
    FaIconComponent
  ],
  styleUrls: ['./change-profile-data.component.scss']
})
export class ChangeProfileDataComponent extends BaseFormComponent implements OnInit {
  @Input() userData!: User;
  @Output() updateData$ = new EventEmitter<User>();
  @Output() cancelEdit$ = new EventEmitter<void>();

  goBack(): void {
    this.cancelEdit$.emit();
  }

  showPasswordFields = false;

  constructor(
    formBuilder: UntypedFormBuilder,
    private profileService: ProfileService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {
    super(formBuilder);
  }

  override ngOnInit() {
    super.ngOnInit();
    this.form.get('confirmPassword')?.setValidators([
      Validators.required,
      this.matchPassword.bind(this),
    ]);
  }

  protected setupForm(): { [key: string]: UntypedFormControl } {
    return {
      login: new UntypedFormControl(this.userData?.login || '', [
        Validators.required,
        Validators.minLength(3),
      ]),
      email: new UntypedFormControl(this.userData?.email || '', [
        Validators.required,
        Validators.email,
      ]),
    };
  }

  togglePasswordChange(): void {
    this.showPasswordFields = !this.showPasswordFields;

    if (this.showPasswordFields) {
      this.form.addControl('oldPassword', new UntypedFormControl('', Validators.required));
      this.form.addControl('newPassword', new UntypedFormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/),
      ]));
      this.form.addControl('confirmPassword', new UntypedFormControl('', [
        Validators.required,
        this.matchPassword.bind(this),
      ]));
    } else {
      this.form.removeControl('oldPassword');
      this.form.removeControl('newPassword');
      this.form.removeControl('confirmPassword');
    }
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    }

    const collectedData: User = this.collectData();

    this.profileService.updateProfileData(collectedData).subscribe({
      next: (response) => {
        this.snackBar.open('Profil został pomyślnie zaktualizowany!', 'Zamknij', {
          duration: 3000
        });
        this.updateData$.emit(response);
        this.router.navigate(['/dashboard']);
      },
      error: (errorResponse) => {
        if (errorResponse.status === 422) {
          this.snackBar.open('Niepoprawne obecne hasło!', 'Zamknij', {
            duration: 3000
          });
        } else {
          this.snackBar.open('Wystąpił błąd podczas aktualizacji profilu.', 'Zamknij', {
            duration: 3000
          });
        }
      },
    });
  }

  private matchPassword(control: UntypedFormControl): { [key: string]: boolean } | null {
    if (control.value !== this.form.get('newPassword')?.value) {
      return {mismatch: true};
    }
    return null;
  }

  collectData(): User {
    return {
      ...this.userData,
      login: this.form.get('login')?.value,
      email: this.form.get('email')?.value,
      oldPassword: this.showPasswordFields ? this.form.get('oldPassword')?.value : undefined,
      newPassword: this.showPasswordFields ? this.form.get('newPassword')?.value : undefined,
    };
  }

  onKeyDownHandler(event: KeyboardEvent): void {
    if (event.key === 'Enter' || event.key === ' ') {
      this.goBack();
    }
  }

  protected readonly faArrowLeft = faArrowLeft;
}
