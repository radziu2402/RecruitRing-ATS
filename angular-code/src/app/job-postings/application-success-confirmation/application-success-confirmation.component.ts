import { Component } from '@angular/core';
import {RouterLink} from "@angular/router";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {faArrowLeft} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-application-success-confirmation',
  standalone: true,
  imports: [
    RouterLink,
    FaIconComponent
  ],
  templateUrl: './application-success-confirmation.component.html',
  styleUrl: './application-success-confirmation.component.scss'
})
export class ApplicationSuccessConfirmationComponent {

  protected readonly faArrowLeft = faArrowLeft;
}
