import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../core/service/security/auth.service';
import {NgClass, NgIf} from '@angular/common';
import {FormsModule, ReactiveFormsModule, UntypedFormControl, Validators} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {catchError} from 'rxjs/operators';
import {of} from 'rxjs';

@Component({
  selector: 'app-set-new-password',
  standalone: true,
  imports: [
    NgIf,
    NgClass,
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './set-new-password.component.html',
  styleUrls: ['./set-new-password.component.scss']
})
export class SetNewPasswordComponent implements OnInit {
  form!: { [key: string]: UntypedFormControl };
  token: string = '';
  tokenValid: boolean = true;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly snackBar: MatSnackBar
  ) {
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParams['token'];
    this.verifyToken();

    this.form = {
      password: new UntypedFormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/),
      ]),
      confirmPassword: new UntypedFormControl('', Validators.required),
    };

    this.form['confirmPassword'].setValidators([
      Validators.required,
      this.matchPassword.bind(this)
    ]);
  }

  verifyToken(): void {
    this.authService.verifyResetToken(this.token)
      .pipe(
        catchError(() => {
          this.tokenValid = false;
          this.router.navigate(['/error-page']);
          return of(false);
        })
      )
      .subscribe({
        next: (isValid: boolean) => {
          if (!isValid) {
            this.tokenValid = false;
            this.router.navigate(['/error-page']);
          }
        }
      });
  }

  private matchPassword(control: UntypedFormControl): { [key: string]: boolean } | null {
    if (control.value !== this.form['password'].value) {
      return {mismatch: true};
    }
    return null;
  }

  onSubmit(): void {
    if (this.isFormValid() && this.tokenValid) {
      const newPassword = this.form['password'].value;

      this.authService.confirmResetPassword(this.token, newPassword).subscribe({
        next: () => {
          this.showSnackBar('Hasło zostało zmienione pomyślnie!', 'Zamknij');
          this.router.navigate(['/login']);
        },
        error: () => {
          this.showSnackBar('Błąd podczas zmiany hasła.', 'Zamknij');
        }
      });
    } else if (!this.tokenValid) {
      this.showSnackBar('Token wygasł lub jest niepoprawny.', 'Zamknij');
    } else {
      this.showSnackBar('Hasła nie są takie same.', 'Zamknij');
    }
  }

  protected isFormValid(): boolean {
    return this.form['password'].valid && this.form['confirmPassword'].valid;
  }

  private showSnackBar(message: string, action: string): void {
    this.snackBar.open(message, action, {
      duration: 5000,
      horizontalPosition: 'center'
    });
  }
}
