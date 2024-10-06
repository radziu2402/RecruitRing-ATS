import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {faEye, faEyeSlash} from "@fortawesome/free-solid-svg-icons";
import {NgClass, NgIf} from '@angular/common';
import {Router} from "@angular/router";
import {AuthService} from "../../core/service/security/auth.service";


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    FontAwesomeModule,
    NgIf,
    NgClass
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  login: string = "";
  password: string = "";
  passwordVisible: boolean = false;

  protected readonly faEyeSlash = faEyeSlash;

  constructor(private readonly authService: AuthService, private readonly router: Router) {
  }

  submit() {
    if (this.login && this.password) {
      this.authService.login(this.login, this.password).subscribe({
        next: () => {
          this.router.navigate(["dashboard/home"]).then(() => {
            console.log('Przekierowano na stronę główną');
          }).catch(err => {
            console.error('Błąd podczas nawigacji:', err);
          });
        },
        error: (err) => {
          console.error('Błąd podczas logowania:', err);
        }
      });
    } else {
      console.error('Proszę wypełnić wszystkie pola.');
    }
  }

  resetPassword(): void {
    this.router.navigate(['/reset-password']);
  }


  togglePasswordVisibility(): void {
    this.passwordVisible = !this.passwordVisible;
    const passwordInput = document.getElementById('loginPassword') as HTMLInputElement;
    passwordInput.type = this.passwordVisible ? 'text' : 'password';
  }

  protected readonly faEye = faEye;
}
