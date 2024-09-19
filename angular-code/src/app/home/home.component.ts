import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    MatButton
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  constructor(private router: Router) {}

  navigateToLogin() {
    this.router.navigate(['/login']);
  }

  navigateToJobListings() {
    this.router.navigate(['/job-listings']);
  }
}
