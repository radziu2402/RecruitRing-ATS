import {Component, Input, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import {AuthService} from "../service/security/auth.service";
import {Subscription} from "rxjs";

@Component({
  standalone: true,
  imports: [],
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  @Input() pageTitle!: string;
  @Input() logoSrc!: string;

  isLoggedIn: boolean = false;
  private authSubscription!: Subscription;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    console.log('Initial login status:', this.isLoggedIn);

    this.authSubscription = this.authService.authStatus$.subscribe(status => {
      console.log('Auth status changed:', status);
      this.isLoggedIn = status;
    });
  }

  navigateOnClick(event: Event): void {
    event.preventDefault();
    if (this.isLoggedIn) {
      this.router.navigate(['/dashboard/home']);
    } else {
      this.router.navigate(['/']);
    }
  }
}
