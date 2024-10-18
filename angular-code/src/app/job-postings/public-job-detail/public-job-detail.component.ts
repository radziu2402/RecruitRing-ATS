import {Component, Input, OnInit} from '@angular/core';
import {PublicJobPosting} from "../model/public-job-posting.model";
import {PublicJobService} from "../service/public-job.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgForOf, NgIf} from "@angular/common";
import {faArrowLeft} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {mapWorkType} from "../service/work-type-mapper";

@Component({
  selector: 'app-public-job-detail',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    FaIconComponent
  ],
  templateUrl: './public-job-detail.component.html',
  styleUrl: './public-job-detail.component.scss'
})
export class PublicJobDetailComponent implements OnInit {
  @Input() offerCode!: string;
  jobDetail!: PublicJobPosting;

  constructor(private readonly jobService: PublicJobService, private readonly route: ActivatedRoute, private readonly router: Router) {}

  ngOnInit(): void {
    const offerCode = this.offerCode || this.route.snapshot.paramMap.get('offerCode');
    if (offerCode) {
      this.jobService.getJobByOfferCode(offerCode).subscribe((job) => {
        this.jobDetail = job;
      });
    }
  }

  formatDate(dateArray: [number, number, number]): string {
    const [year, month, day] = dateArray;
    return `${day}-${month}-${year}`;
  }

  navigateToApplication() {
    this.router.navigate(['/jobs/apply', this.jobDetail.offerCode], {
      state: { jobTitle: this.jobDetail.title.name }
    });  }

  goBack() {
    this.router.navigate(['/jobs']);
  }

  onKeyDownHandler(event: KeyboardEvent) {
    if (event.key === 'Enter' || event.key === ' ') {
      this.goBack();
    }
  }

  protected readonly faArrowLeft = faArrowLeft;
  protected readonly mapWorkType = mapWorkType;
}
