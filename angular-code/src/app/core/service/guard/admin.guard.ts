import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {AuthService} from "../security/auth.service";

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private readonly authService: AuthService, private readonly router: Router) {
  }

  canActivate(): boolean {
    const userRole = this.authService.getRole();
    if (userRole === 'ADMINISTRATOR') {
      return true;
    } else {
      this.router.navigate(['/dashboard']);
      return false;
    }
  }
}
