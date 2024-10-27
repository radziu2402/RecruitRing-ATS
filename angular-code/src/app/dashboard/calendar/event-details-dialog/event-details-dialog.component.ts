import {Component, Inject, LOCALE_ID} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {CommonModule, registerLocaleData} from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import localePl from '@angular/common/locales/pl';

registerLocaleData(localePl);

@Component({
  selector: 'app-event-details-dialog',
  standalone: true,
  templateUrl: './event-details-dialog.component.html',
  styleUrls: ['./event-details-dialog.component.scss'],
  imports: [CommonModule, MatButtonModule, MatDialogActions, MatDialogContent, MatDialogTitle],
  providers: [
    { provide: LOCALE_ID, useValue: 'pl' }
  ]
})
export class EventDetailsDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<EventDetailsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
