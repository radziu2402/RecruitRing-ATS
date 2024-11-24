import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {NgIf, registerLocaleData} from "@angular/common";
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MAT_DATE_LOCALE,
  MAT_NATIVE_DATE_FORMATS,
  MatNativeDateModule,
  NativeDateAdapter
} from "@angular/material/core";
import localePl from '@angular/common/locales/pl';
import {
  NgxMatTimepickerComponent,
  NgxMatTimepickerDirective,
  NgxMatTimepickerModule,
  NgxMatTimepickerToggleComponent
} from "ngx-mat-timepicker";
import {MatCheckbox} from "@angular/material/checkbox";

registerLocaleData(localePl);

@Component({
  selector: 'app-add-event-dialog',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatInputModule,
    MatButtonModule,
    MatNativeDateModule,
    NgIf,
    NgxMatTimepickerToggleComponent,
    NgxMatTimepickerComponent,
    NgxMatTimepickerDirective,
    NgxMatTimepickerModule,
    MatCheckbox,
    FormsModule
  ],
  providers: [
    {provide: MAT_DATE_LOCALE, useValue: 'pl-PL'},
    {provide: DateAdapter, useClass: NativeDateAdapter},
    {provide: MAT_DATE_FORMATS, useValue: MAT_NATIVE_DATE_FORMATS}
  ],
  templateUrl: './add-event-dialog.component.html',
  styleUrls: ['./add-event-dialog.component.scss']
})
export class AddEventDialogComponent implements OnInit {
  eventForm: FormGroup;
  showSendEmailCheckbox = false;
  sendEmail = false;

  constructor(
    private dialogRef: MatDialogRef<AddEventDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder
  ) {
    this.eventForm = this.fb.group({
      title: [data?.title || '', Validators.required],
      startDate: ['', Validators.required],
      startTime: ['', [Validators.required, Validators.pattern('^([01]?[0-9]|2[0-3]):[0-5][0-9]$')]],
      endDate: ['', Validators.required],
      endTime: ['', [Validators.required, Validators.pattern('^([01]?[0-9]|2[0-3]):[0-5][0-9]$')]],
      description: [data?.description || ''],
      sendEmail: [false]
    });
  }

  ngOnInit(): void {
    this.showSendEmailCheckbox = !!this.data.showSendEmailCheckbox;
  }

  submit() {
    if (this.eventForm.valid) {
      const {title, description, startDate, startTime, endDate, endTime, sendEmail} = this.eventForm.value;

      try {
        if (startDate && startTime && endDate && endTime) {
          const startDateString = startDate.toLocaleDateString('en-CA');
          const endDateString = endDate.toLocaleDateString('en-CA');

          const start = new Date(`${startDateString}T${startTime}:00`).toISOString();
          const end = new Date(`${endDateString}T${endTime}:00`).toISOString();

          const eventData = {
            title,
            description,
            start,
            end,
            sendEmail
          };

          this.dialogRef.close(eventData);
        }
      } catch (error) {
        console.error(error);
      }
    }
  }

  close() {
    this.dialogRef.close();
  }
}
