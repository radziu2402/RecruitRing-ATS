import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PublicJobService {
  private readonly apiUrl = `${environment.api}public-jobs`;

  constructor(private readonly http: HttpClient) {
  }

  getAllJobPostings(page: number, size: number, filters: any): Observable<any> {
    let params = new HttpParams().set('page', page).set('size', size);

    if (filters.location) {
      params = params.set('locationId', filters.location);
    }
    if (filters.title) {
      params = params.set('titleId', filters.title);
    }
    if (filters.workType) {
      params = params.set('workType', filters.workType);
    }
    if (filters.jobCategory) {
      params = params.set('jobCategoryId', filters.jobCategory);
    }

    return this.http.get<any>(this.apiUrl, {params});
  }
}
