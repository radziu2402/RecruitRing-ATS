import { CalendarNativeDateFormatter, DateFormatterParams } from 'angular-calendar';

export class CustomDateFormatter extends CalendarNativeDateFormatter {

  // Nadpisanie formatu godziny dla widoku dnia
  public override dayViewHour({ date, locale }: DateFormatterParams): string {
    return new Intl.DateTimeFormat(locale, { hour: '2-digit', minute: '2-digit', hour12: false }).format(date);
  }

  // Nadpisanie formatu godziny dla widoku tygodnia (jeśli potrzebne)
  public override weekViewHour({ date, locale }: DateFormatterParams): string {
    return new Intl.DateTimeFormat(locale, { hour: '2-digit', minute: '2-digit', hour12: false }).format(date);
  }
}
