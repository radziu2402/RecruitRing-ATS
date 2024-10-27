import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from "../../../../environments/environment";
import {Title} from "../model/title.model";

@Injectable({
  providedIn: 'root'
})
export class TitleService {
  private readonly apiUrl = `${environment.api}titles`;

  constructor(private readonly http: HttpClient) {
  }

  searchTitles(query: string): Observable<any[]> {
    const headers = {'X-Skip-Spinner': 'true'};
    return this.http.get<any[]>(`${this.apiUrl}/search`, {params: {query}, headers});
  }

  createTitle(title: string): Observable<Title> {
    const headers = {'X-Skip-Spinner': 'true'};
    return this.http.post<Title>(this.apiUrl, title, { headers });
  }
}
