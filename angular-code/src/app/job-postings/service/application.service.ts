import {Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

  private readonly apiUrl = environment.api + 'apply';

  constructor(private readonly http: HttpClient) {
  }

  submitApplication(formData: FormData): Observable<any> {
    return this.http.post(this.apiUrl, formData);
  }
}
