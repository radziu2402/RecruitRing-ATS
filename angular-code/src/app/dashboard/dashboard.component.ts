import {Component, OnInit} from '@angular/core';
import {Router, RouterLink, RouterOutlet} from "@angular/router";
import {AuthService} from "../core/service/security/auth.service";
import {faSignOutAlt, faUser} from '@fortawesome/free-solid-svg-icons';
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    FaIconComponent,
    NgIf
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  faSignOutAlt = faSignOutAlt;

  isAdmin: boolean = false;

  constructor(private readonly authService: AuthService, private readonly router: Router) {
  }

  ngOnInit() {
    this.checkIfAdmin();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['']);
  }

  navigateToProfile() {
    this.router.navigate(['/dashboard/profile'])
  }

  checkIfAdmin() {
    const role = this.authService.getRole();
    this.isAdmin = role === 'ADMINISTRATOR';
  }

  protected readonly faUser = faUser;
}
