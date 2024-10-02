import {Component} from '@angular/core';
import {AuthService} from "../../core/service/security/auth.service";
import {NgClass, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {faArrowLeft} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

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

  constructor(private authService: AuthService, private router: Router, private snackBar: MatSnackBar) {
  }

  submit() {
    if (this.email) {
      this.showNotification('Jeśli to konto istnieje, link do resetu hasła został wysłany na podany adres e-mail.');

      this.goBack();

      Promise.resolve().then(() => {
        this.authService.resetPassword(this.email).subscribe({
          next: () => {
          },
          error: () => {
          }
        });
      });
    }
  }

  showNotification(message: string) {
    this.snackBar.open(message, 'Zamknij', {duration: 3000});
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
