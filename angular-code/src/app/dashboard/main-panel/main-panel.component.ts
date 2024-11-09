import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {DashboardService} from "./service/dashboard.service";

@Component({
  selector: 'app-main-panel',
  standalone: true,
  imports: [],
  templateUrl: './main-panel.component.html',
  styleUrls: ['./main-panel.component.scss']
})
export class MainPanelComponent implements OnInit {
  stats = {
    openRecruitments: 0,
    newCandidates: 0,
    scheduledMeetings: 0,
    avgTimeToHire: 0,
    candidateRatings: 0,
    rejectedCandidates: 0,
    hiredCandidates: 0
  };

  constructor(
    private readonly router: Router,
    private readonly dashboardService: DashboardService
  ) {}

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats(): void {
    this.dashboardService.getDashboardStats().subscribe(data => {
      this.stats = {
        openRecruitments: data.openRecruitments,
        newCandidates: data.newCandidates,
        scheduledMeetings: data.scheduledMeetings,
        avgTimeToHire: data.avgTimeToHire,
        candidateRatings: data.candidateRatings,
        rejectedCandidates: data.rejectedCandidates,
        hiredCandidates: data.hiredCandidates
      };
    });
  }

  navigateTo(section: string): void {
    this.router.navigate([`/dashboard/${section}`]);
  }
}
