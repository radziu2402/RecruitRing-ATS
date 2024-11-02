import {Component, Inject, LOCALE_ID} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {CommonModule} from '@angular/common';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {EventService} from "../service/events.service";
import {ConfirmationDialogComponent} from "../../job-management/confirmation-dialog/confirmation-dialog.component";

@Component({
  selector: 'app-event-details-dialog',
  standalone: true,
  templateUrl: './event-details-dialog.component.html',
  styleUrls: ['./event-details-dialog.component.scss'],
  imports: [CommonModule, MatButtonModule, MatIconModule, MatDialogActions, MatDialogContent, MatDialogTitle],
  providers: [
    {provide: LOCALE_ID, useValue: 'pl'}
  ]
})
export class EventDetailsDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<EventDetailsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialog: MatDialog,
    private eventService: EventService
  ) {
  }

  openConfirmDialog(): void {
    const confirmDialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: 'Czy na pewno chcesz usunąć to wydarzenie?'
    });

    confirmDialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.eventService.deleteEvent(this.data.id).subscribe(() => {
          this.dialogRef.close({deleted: true});
        });
      }
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
