import {Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ApplicationStatusDTO} from "../model/application-status.model";

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

  private readonly apiUrl = environment.api;

  constructor(private readonly http: HttpClient) {}

  submitApplication(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/apply`, formData);
  }

  getApplicationStatus(applicationCode: string): Observable<ApplicationStatusDTO> {
    return this.http.get<ApplicationStatusDTO>(`${this.apiUrl}/applications/status/${applicationCode}`);
  }
}
