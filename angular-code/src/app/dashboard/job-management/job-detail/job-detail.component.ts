import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {JobPosting} from "../model/job-posting.model";
import {JobService} from "../service/job.service";
import {NgForOf, NgIf} from "@angular/common";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ConfirmationDialogComponent} from "../../../confirmation-dialog/confirmation-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {mapWorkType} from "../../../job-postings/service/work-type-mapper";


@Component({
  selector: 'app-job-details',
  templateUrl: './job-detail.component.html',
  standalone: true,
  imports: [
    NgForOf,
    NgIf
  ],
  styleUrls: ['./job-detail.component.scss']
})
export class JobDetailComponent implements OnInit {
  job!: JobPosting;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly jobService: JobService,
    private readonly router: Router,
    private readonly snackBar: MatSnackBar,
    private readonly dialog: MatDialog
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const offerCode = params.get('offerCode');
      if (offerCode) {
        this.loadJob(offerCode);
      }
    });
  }

  private loadJob(offerCode: string): void {
    this.jobService.getJobByOfferCode(offerCode).subscribe((job) => {
      this.job = job;
    });
  }


  formatDate(dateArray: [number, number, number]): string {
    const [year, month, day] = dateArray;
    return `${day}-${month}-${year}`;
  }

  goBack() {
    this.router.navigate(['/dashboard/jobs']).then(() => {
    }).catch((error) => {
      console.error('Navigation error:', error);
    });
  }

  editJob(): void {
    const offerCode = this.job.offerCode;
    this.router.navigate(['/dashboard/edit-job', offerCode]);
  }

  deleteJob(): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '300px',
      data: 'Czy na pewno chcesz usunąć tę ofertę pracy?'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.jobService.deleteJobByOfferCode(this.job.offerCode).subscribe(() => {
          this.router.navigate(['/dashboard/jobs']).then(() => {
            this.showNotification('Oferta pracy została usunięta pomyślnie.');
          });
        });
      }
    });
  }

  showNotification(message: string) {
    this.snackBar.open(message, 'Zamknij', {
      duration: 3000,
    });
  }

  protected readonly mapWorkType = mapWorkType;
}
