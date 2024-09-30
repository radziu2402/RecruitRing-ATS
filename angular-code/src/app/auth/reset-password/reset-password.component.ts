import {Component} from '@angular/core';
import {AuthService} from "../../core/service/security/auth.service";
import {NgClass, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {faArrowLeft} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {Router} from "@angular/router";

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [
    NgIf,
    NgClass,
    FormsModule,
    FaIconComponent
  ],
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent {
  email: string = "";

  constructor(private authService: AuthService, private router: Router) {
  }

  submit() {
    if (this.email) {
      this.authService.resetPassword(this.email).subscribe({
        next: () => {
          console.log('Link do zresetowania hasła został wysłany na adres e-mail:', this.email);
        },
        error: (err) => {
          console.error('Błąd podczas resetowania hasła:', err);
        }
      });
    }
  }

  goBack() {
    this.router.navigate(['/login']);
  }

  onKeyDownHandler(event: KeyboardEvent) {
    if (event.key === 'Enter' || event.key === ' ') {
      this.goBack();
    }
  }

  protected readonly faArrowLeft = faArrowLeft;
}
