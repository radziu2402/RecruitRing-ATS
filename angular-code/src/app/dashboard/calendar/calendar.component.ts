import {Component, OnInit} from '@angular/core';
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
import {addDays, addMonths, subDays, subMonths} from 'date-fns';
import {NgIf, registerLocaleData} from '@angular/common';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import localePl from '@angular/common/locales/pl';
import {CustomDateFormatter} from './custom-date.formatter';
import {MatDialog} from "@angular/material/dialog";
import {EventDetailsDialogComponent} from "./event-details-dialog/event-details-dialog.component";
import {ActivatedRoute} from '@angular/router';
import {EventDTO} from "./model/event.model";
import {EventService} from "./service/events.service";
import {AddEventDialogComponent} from "./add-event-dialog/add-event-dialog.component";
import {MatNativeDateModule} from "@angular/material/core";

registerLocaleData(localePl);

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [CalendarModule, NgIf, MatNativeDateModule],
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
    CalendarEventTitleFormatter,
  ],
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss'],
})
export class CalendarComponent implements OnInit {
  viewDate: Date = new Date();
  view: CalendarView = CalendarView.Month;
  CalendarView = CalendarView;
  locale: string = 'pl';
  dayStartHour = 7;
  dayEndHour = 17;

  events: CalendarEvent[] = [];

  constructor(
    private readonly dialog: MatDialog,
    private readonly route: ActivatedRoute,
    private readonly eventService: EventService
  ) {
  }

  ngOnInit(): void {
    this.route.data.subscribe(({events}) => {
      this.events = events.map((event: EventDTO) => ({
        id: event.id,
        start: new Date(event.start),
        end: new Date(event.end),
        title: event.title,
        color: {primary: '#1e90ff', secondary: '#D1E8FF'},
        meta: {description: event.description}
      }));
    });
  }


  setDynamicHoursForDay(date: Date): void {
    const dayEvents = this.events.filter(event =>
      event.start.toDateString() === date.toDateString()
    );

    if (dayEvents.length) {
      const startHours = dayEvents.map(event => event.start.getHours());
      const endHours = dayEvents.map(event => event.end ? event.end.getHours() : event.start.getHours());
      this.dayStartHour = Math.min(...startHours, 7);
      this.dayEndHour = Math.max(...endHours, 17);
    } else {
      this.dayStartHour = 7;
      this.dayEndHour = 17;
    }
  }

  eventClicked(event: CalendarEvent): void {
    const dialogRef = this.dialog.open(EventDetailsDialogComponent, {
      data: event,
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result?.deleted) {
        this.events = this.events.filter(e => e.id !== event.id);
      }
    });
  }

  previousView(): void {
    if (this.view === CalendarView.Month) {
      this.viewDate = subMonths(this.viewDate, 1);
    } else if (this.view === CalendarView.Week) {
      this.viewDate = subDays(this.viewDate, 7);
    } else if (this.view === CalendarView.Day) {
      this.viewDate = subDays(this.viewDate, 1);
      this.setDynamicHoursForDay(this.viewDate);
    }
  }

  nextView(): void {
    if (this.view === CalendarView.Month) {
      this.viewDate = addMonths(this.viewDate, 1);
    } else if (this.view === CalendarView.Week) {
      this.viewDate = addDays(this.viewDate, 7);
    } else if (this.view === CalendarView.Day) {
      this.viewDate = addDays(this.viewDate, 1);
      this.setDynamicHoursForDay(this.viewDate);
    }
  }

  resetView(): void {
    this.viewDate = new Date();
    this.setView(CalendarView.Month);
  }

  setView(view: CalendarView) {
    this.view = view;
    if (view === CalendarView.Day) {
      this.setDynamicHoursForDay(this.viewDate);
    }
  }

  dayClicked({date}: { date: Date; events: CalendarEvent[] }): void {
    this.viewDate = date;
    this.setView(CalendarView.Day);
    this.setDynamicHoursForDay(date);
  }

  get currentMonth(): string {
    const options: Intl.DateTimeFormatOptions = {month: 'long', year: 'numeric'};
    return new Intl.DateTimeFormat(this.locale, options).format(this.viewDate);
  }

  get currentDay(): string {
    const options: Intl.DateTimeFormatOptions = {weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'};
    return new Intl.DateTimeFormat(this.locale, options).format(this.viewDate);
  }

  openAddEventDialog(): void {
    const dialogRef = this.dialog.open(AddEventDialogComponent, {
      width: '400px',
      data: {
        showSendEmailCheckbox: false
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.eventService.createEvent(result).subscribe(() => {
          this.eventService.getMyEvents().subscribe((events) => {
            this.events = events.map((event: EventDTO) => ({
              id: event.id,
              start: new Date(event.start),
              end: new Date(event.end),
              title: event.title,
              color: {primary: '#1e90ff', secondary: '#D1E8FF'},
              meta: {description: event.description}
            }));
          });
        });
      }
    });

  }
}
