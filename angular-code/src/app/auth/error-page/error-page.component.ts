import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {faTriangleExclamation} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome"; // Import ikony


@Component({
  selector: 'app-error-page',
  standalone: true,
  imports: [
    RouterLink,
    FaIconComponent
  ],
  templateUrl: './error-page.component.html',
  styleUrl: './error-page.component.scss'
})
export class ErrorPageComponent {
  protected readonly faTriangleExclamation = faTriangleExclamation;
}
