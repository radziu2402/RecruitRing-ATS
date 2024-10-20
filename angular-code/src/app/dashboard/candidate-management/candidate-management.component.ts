import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-candidate-management',
  standalone: true,
  imports: [RouterModule, NgForOf],
  templateUrl: './candidate-management.component.html',
  styleUrls: ['./candidate-management.component.scss']
})
export class CandidateManagementComponent {

  candidates = [
    {
      id: 1,
      name: 'Emilia Skoczek',
      email: 'emilia.skoczek@gmailer.com',
      phone: '123 456 789',
      location: 'Poznań, wielkopolskie',
      status: 'W trakcie',
    },
    {
      id: 2,
      name: 'Bartosz Oracki',
      email: 'boracki@gmailer.pl',
      phone: '123 456 789',
      location: 'Tłuszcz Gdański, pomorskie',
      status: 'Zadanie rekrutacyjne',
    },
    {
      id: 3,
      name: 'Renata Cwalina',
      email: 'rcwalina@gmailer.pl',
      phone: '123 456 789',
      location: 'Poznań, wielkopolskie',
      status: 'Zakończona',
    }
  ];

  constructor() {}

  goToCandidateDetails(candidateId: number) {
    console.log(`Navigating to candidate ${candidateId}`);
    // Tu możesz dodać logikę do przeniesienia do widoku szczegółów kandydata
  }

}
