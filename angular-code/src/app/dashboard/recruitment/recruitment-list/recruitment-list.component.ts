import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-recruitment-list',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './recruitment-list.component.html',
  styleUrl: './recruitment-list.component.scss'
})
export class RecruitmentListComponent {

  constructor(private router: Router) {
  }

  recruitments = [
    {id: 1, title: 'Customer Success Consultant', location: 'Poznań', newCandidates: 20, candidates: 37},
    {id: 2, title: 'System poleceń', location: 'cała Polska', newCandidates: 2, candidates: 29},
    {id: 3, title: 'On-line Marketing Specialist', location: 'Stockholm', newCandidates: 8, candidates: 43},
    {id: 4, title: 'Junior Account Manager', location: 'Poznań', newCandidates: 1, candidates: 24}
  ];

  filteredRecruitments = [...this.recruitments]; // Initially all

  applyFilter(filterType: string) {
    console.log(`Filtering by: ${filterType}`);
    // Apply filtering logic here
  }

  onSearch(event: any) {
    const query = event.target.value.toLowerCase();
    this.filteredRecruitments = this.recruitments.filter(r =>
      r.title.toLowerCase().includes(query) || r.location.toLowerCase().includes(query)
    );
  }

  goToRecruitment(id: number) {
    this.router.navigate(['/dashboard/recruitment', id]);
  }
}
