import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {EventService} from "../service/events.service";
import {EventDTO} from "../model/event.model";

export const eventsResolver: ResolveFn<Observable<EventDTO[] | null>> = () => {
  const eventService = inject(EventService);
  return eventService.getMyEvents().pipe(
    catchError(() => of(null))
  );
};
