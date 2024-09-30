import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ReactiveFormsModule, UntypedFormBuilder, UntypedFormControl, Validators} from "@angular/forms";
import {User} from "../../model/user";
import {ProfileService} from "../../service/profile.service";
import {throwError} from "rxjs";
import {CommonModule} from "@angular/common";
import {BaseFormComponent} from "../../../core/base-form/base-form.component";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'change-profile-data',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatButton],
  templateUrl: './change-profile-data.component.html',
  styleUrls: ['./change-profile-data.component.scss'],
})
export class ChangeProfileDataComponent extends BaseFormComponent implements OnInit {
  @Input()
  userData!: User;

  @Output()
  updateData$ = new EventEmitter<User>();

  constructor(formBuilder: UntypedFormBuilder, private profileService: ProfileService) {
    super(formBuilder);
  }

  submit(): void {
    const collectedData: User = this.collectData();
    console.log(collectedData);
    this.profileService.updateProfileData(collectedData).subscribe(
      {
        next: (data) => this.updateData$.emit(data),
        error: () => throwError(() => new Error("Error"))
      }
    )
  }

  protected setupForm(): { [key: string]: UntypedFormControl } {
    return {
      login: new UntypedFormControl(this.userData.login, [
        Validators.required,
        Validators.minLength(3),
      ]),
      email: new UntypedFormControl(this.userData.email, [
        Validators.required,
        Validators.email,
      ]),
      password: new UntypedFormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/),
      ]),
      confirmPassword: new UntypedFormControl('', [
        Validators.required,
      ]),
    };
  }

  override ngOnInit() {
    super.ngOnInit();
    this.form.get('confirmPassword')?.setValidators([
      Validators.required,
      this.matchPassword.bind(this),
    ]);
  }

// Metoda sprawdzająca zgodność haseł
  private matchPassword(control: UntypedFormControl): { [key: string]: boolean } | null {
    if (control.value !== this.form.get('password')?.value) {
      return { mismatch: true };
    }
    return null;
  }


  collectData(): User {
    return {
      firstName: this.userData.firstName,
      lastName: this.userData.lastName,
      dateOfBirth: this.userData.dateOfBirth,
      position: this.userData.position,
      login: this.form.get('login')?.value,
      password: this.form.get('password')?.value,
      email: this.form.get('email')?.value
    }
  }
}
