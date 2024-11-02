import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {EventDTO} from "../model/event.model";
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private readonly apiUrl = `${environment.api}events`;

  constructor(private http: HttpClient) {}

  getMyEvents(): Observable<EventDTO[]> {
    return this.http.get<EventDTO[]>(`${this.apiUrl}/my-events`);
  }

  createEvent(event: EventDTO): Observable<EventDTO> {
    return this.http.post<EventDTO>(`${this.apiUrl}`, event);
  }

  deleteEvent(eventId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${eventId}`);
  }
}
