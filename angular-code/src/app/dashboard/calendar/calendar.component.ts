import {Component} from '@angular/core';
import {
  CalendarA11y,
  CalendarDateFormatter,
  CalendarEvent,
  CalendarEventTitleFormatter,
  CalendarModule,
  CalendarUtils,
  CalendarView,
  DateAdapter
} from 'angular-calendar';
import {addDays, addHours, addMonths, subDays, subMonths} from 'date-fns';
import {NgIf} from "@angular/common";
import {adapterFactory} from "angular-calendar/date-adapters/date-fns";
import 'zone.js';
import { registerLocaleData } from '@angular/common';
import localePl from '@angular/common/locales/pl';
import {CustomDateFormatter} from "./custom-date.formatter";


// Rejestracja polskiej lokalizacji
registerLocaleData(localePl);
@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [
    CalendarModule,
    NgIf,
  ],
  providers: [
    {
      provide: DateAdapter,
      useFactory: adapterFactory,
    },
    CalendarUtils,
    CalendarA11y,
    {
      provide: CalendarDateFormatter,
      useClass: CustomDateFormatter
    },
    CalendarEventTitleFormatter
  ],
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent {
  viewDate: Date = new Date();
  view: CalendarView = CalendarView.Month;
  CalendarView = CalendarView;

  events: CalendarEvent[] = [
    {
      start: addHours(new Date(), 2),
      end: addHours(new Date(), 4),
      title: 'Spotkanie rekrutacyjne',
      color: {primary: '#ad2121', secondary: '#FAE3E3'},
    },
    {
      start: addHours(new Date(), 6),
      end: addHours(new Date(), 7),
      title: 'Rozmowa kwalifikacyjna',
      color: {primary: '#1e90ff', secondary: '#D1E8FF'},
    }
  ];

  // Zmiana widoku na poprzedni miesiąc lub tydzień
  previousView(): void {
    if (this.view === CalendarView.Month) {
      this.viewDate = subMonths(this.viewDate, 1); // Zmiana o jeden miesiąc do tyłu
    } else if (this.view === CalendarView.Week || this.view === CalendarView.Day) {
      this.viewDate = subDays(this.viewDate, 7); // Zmiana o jeden tydzień do tyłu
    }
  }

  // Zmiana widoku na następny miesiąc lub tydzień
  nextView(): void {
    if (this.view === CalendarView.Month) {
      this.viewDate = addMonths(this.viewDate, 1); // Zmiana o jeden miesiąc do przodu
    } else if (this.view === CalendarView.Week || this.view === CalendarView.Day) {
      this.viewDate = addDays(this.viewDate, 7); // Zmiana o jeden tydzień do przodu
    }
  }

  setView(view: CalendarView) {
    this.view = view;
  }

  dayClicked({date, events}: { date: Date; events: CalendarEvent[] }): void {
    console.log('Clicked date:', date);
    console.log('Events on this day:', events);
  }

  locale: string = 'pl';
}
