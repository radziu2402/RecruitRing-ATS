import {Component, OnInit} from '@angular/core';
import {NgForOf} from "@angular/common";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-recruitment-management',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './recruitment-management.component.html',
  styleUrl: './recruitment-management.component.scss'
})
export class RecruitmentManagementComponent implements OnInit {
  candidates = [
    {
      name: 'Emilia Skoczek',
      email: 'emilia.skoczek@gmailer.com',
      phone: '123 456 789',
      location: 'Poznań, wielkopolskie',
      stage: 'Analiza CV',
      lastAction: '4 dni temu, Zaaplikowanie',
      salary: '6 - 8 tys. zł',
    },
    {
      name: 'Bartosz Oracki',
      email: 'boracki@gmailer.pl',
      phone: '123 456 789',
      location: 'Tłuszcz Gdański, pomorskie',
      stage: 'Zadanie rekrutacyjne',
      lastAction: '5 dni temu, Wysłanie karty kandydata',
      salary: '4 - 6 tys. zł',
    },
    {
      name: 'Renata Cwalina',
      email: 'rcwalina@gmailer.pl',
      phone: '123 456 789',
      location: 'Poznań, wielkopolskie',
      stage: 'II etap',
      lastAction: '5 dni temu, Decyzja managera',
      salary: '6 - 8 tys. zł',
    },
  ];

  recruitmentId: string | null = null;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Pobranie ID rekrutacji z URL-a
    this.recruitmentId = this.route.snapshot.paramMap.get('id');
    console.log('Recruitment ID:', this.recruitmentId);
  }
}
