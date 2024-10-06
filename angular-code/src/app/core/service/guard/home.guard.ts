import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import {AuthService} from "../security/auth.service";

@Injectable({
  providedIn: 'root'
})
export class HomeGuard implements CanActivate {
  constructor(private readonly authService: AuthService, private readonly router: Router) {}

  canActivate(): boolean {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['dashboard']);
      return false;
    }
    return true;
  }
}
