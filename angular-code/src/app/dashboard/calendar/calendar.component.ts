import {Component} from '@angular/core';
import {
  CalendarA11y,
  CalendarDateFormatter,
  CalendarEvent,
  CalendarEventTitleFormatter,
  CalendarModule,
  CalendarUtils,
  CalendarView,
  DateAdapter,
} from 'angular-calendar';
import {addDays, addHours, addMonths, subDays, subMonths} from 'date-fns';
import {NgIf, registerLocaleData} from '@angular/common';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import localePl from '@angular/common/locales/pl';
import {CustomDateFormatter} from './custom-date.formatter';
import {MatDialog} from "@angular/material/dialog";
import {EventDetailsDialogComponent} from "./event-details-dialog/event-details-dialog.component";

registerLocaleData(localePl);

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [CalendarModule, NgIf],
  providers: [
    {
      provide: DateAdapter,
      useFactory: adapterFactory,
    },
    CalendarUtils,
    CalendarA11y,
    {
      provide: CalendarDateFormatter,
      useClass: CustomDateFormatter,
    },
    CalendarEventTitleFormatter
  ],
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss'],
})
export class CalendarComponent {
  viewDate: Date = new Date();
  view: CalendarView = CalendarView.Month;
  CalendarView = CalendarView;

  constructor(private dialog: MatDialog) {
  }

  events: CalendarEvent[] = [
    {
      start: new Date('2024-10-25T10:00:00'),
      end: new Date('2024-10-25T12:00:00'),
      title: 'Spotkanie rekrutacyjne',
      color: { primary: '#ad2121', secondary: '#FAE3E3' },
      meta: {
        description: 'Spotkanie rekrutacyjne z potencjalnym kandydatem na stanowisko junior developera. Omówienie ścieżki kariery i oczekiwań.'
      }
    },
    {
      start: new Date('2024-10-26T14:00:00'),
      end: new Date('2024-10-26T15:00:00'),
      title: 'Rozmowa kwalifikacyjna',
      color: { primary: '#1e90ff', secondary: '#D1E8FF' },
      meta: {
        description: 'Rozmowa kwalifikacyjna na stanowisko senior managera. Pytania techniczne oraz omówienie umiejętności zarządzania zespołem.'
      }
    },
    {
      start: new Date('2024-10-27T09:00:00'),
      end: new Date('2024-10-27T10:30:00'),
      title: 'Warsztaty zespołowe',
      color: { primary: '#e3bc08', secondary: '#FDF1BA' },
      meta: {
        description: 'Warsztaty dla zespołu IT dotyczące nowoczesnych praktyk DevOps. Wprowadzenie do CI/CD oraz automatyzacji procesów wdrażania.'
      }
    }
  ];


  eventClicked(event: CalendarEvent): void {
    this.dialog.open(EventDetailsDialogComponent, {
      data: event
    });
  }

  previousView(): void {
    if (this.view === CalendarView.Month) {
      this.viewDate = subMonths(this.viewDate, 1);
    } else if (this.view === CalendarView.Week) {
      this.viewDate = subDays(this.viewDate, 7);
    } else if (this.view === CalendarView.Day) {
      this.viewDate = subDays(this.viewDate, 1);
    }
  }

  nextView(): void {
    if (this.view === CalendarView.Month) {
      this.viewDate = addMonths(this.viewDate, 1);
    } else if (this.view === CalendarView.Week) {
      this.viewDate = addDays(this.viewDate, 7);
    } else if (this.view === CalendarView.Day) {
      this.viewDate = addDays(this.viewDate, 1);
    }
  }

  resetView(): void {
    this.viewDate = new Date();
    this.setView(CalendarView.Month);
  }

  setView(view: CalendarView) {
    this.view = view;
  }

  dayClicked({date}: { date: Date; events: CalendarEvent[] }): void {
    this.viewDate = date;
    this.setView(CalendarView.Day);
  }

  locale: string = 'pl';

  get currentMonth(): string {
    const options: Intl.DateTimeFormatOptions = {month: 'long', year: 'numeric'};
    return new Intl.DateTimeFormat(this.locale, options).format(this.viewDate);
  }

  get currentDay(): string {
    const options: Intl.DateTimeFormatOptions = {weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'};
    return new Intl.DateTimeFormat(this.locale, options).format(this.viewDate);
  }
}
